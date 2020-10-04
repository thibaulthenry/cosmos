package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.arguments.WorldPropertiesArguments;
import cosmos.statics.finders.FinderWorldName;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Duplicate extends AbstractCommand {

    public Duplicate() {
        super(
                Arguments.limitCompleteElement(
                        GenericArguments.firstParsing(
                                WorldPropertiesArguments.allChoices(ArgKeys.WORLD, false),
                                WorldNameArguments.disabledChoices(ArgKeys.DISABLED_WORLD)
                        )
                ),
                GenericArguments.string(ArgKeys.NEW_NAME.t)
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        WorldProperties worldProperties = args.<WorldProperties>getOne(ArgKeys.WORLD.t)
                .map(Optional::of)
                .orElse(args.<String>getOne(ArgKeys.DISABLED_WORLD.t).flatMap(FinderWorldProperties::getDisabledWorldProperties))
                .orElseThrow(Outputs.INVALID_ALL_WORLD_CHOICE.asSupplier());

        String copyName = args.<String>getOne(ArgKeys.NEW_NAME.t).orElseThrow(Outputs.INVALID_WORLD_NAME.asSupplier());

        if (FinderWorldName.isImported(copyName)) {
            throw Outputs.EXISTING_WORLD.asException(copyName);
        }

        CompletableFuture<Optional<WorldProperties>> copyTask = Sponge.getServer().copyWorld(worldProperties, copyName);

        copyTask.thenRunAsync(() -> {
            WorldProperties copyWorldProperties;

            try {
                Optional<WorldProperties> optionalCopyWorldProperties = copyTask.get();

                if (!optionalCopyWorldProperties.isPresent()) {
                    src.sendMessage(Outputs.DUPLICATING_WORLD.asText(worldProperties, copyName));
                    return;
                }

                copyWorldProperties = optionalCopyWorldProperties.get();
            } catch (InterruptedException | ExecutionException ignored) {
                src.sendMessage(Outputs.DUPLICATING_WORLD.asText(worldProperties, copyName));
                return;
            }

            copyWorldProperties.setEnabled(worldProperties.isEnabled());
            copyWorldProperties.setLoadOnStartup(worldProperties.loadOnStartup());
            Sponge.getServer().saveWorldProperties(copyWorldProperties);
            src.sendMessage(Outputs.DUPLICATE_WORLD.asText(worldProperties, copyName));
        });
    }
}
