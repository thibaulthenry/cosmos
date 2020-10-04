package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.finders.FinderFile;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

public class Delete extends AbstractCommand {

    public Delete() {
        super(
                Arguments.limitCompleteElement(
                        GenericArguments.firstParsing(
                                WorldNameArguments.unloadedChoices(ArgKeys.UNLOADED_WORLD, false),
                                WorldNameArguments.disabledChoices(ArgKeys.DISABLED_WORLD),
                                WorldNameArguments.exportedChoices(ArgKeys.EXPORTED_WORLD)
                        )
                )
        );
    }

    private static void deleteWorldManually(String worldName) throws CommandException {
        Path worldPath = FinderFile.getWorldPath(worldName)
                .orElseThrow(Outputs.MISSING_EXPORTED_WORLD.asSupplier(worldName));

        try {
            FinderFile.deleteDirectory(worldPath);
        } catch (Exception ignored) {
            throw Outputs.DELETING_EXPORTED_WORLD.asException(worldName);
        }
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> optionalExportedWorldName = args.getOne(ArgKeys.EXPORTED_WORLD.t);

        if (optionalExportedWorldName.isPresent()) {
            String worldName = optionalExportedWorldName.get();
            deleteWorldManually(worldName);
            src.sendMessage(Outputs.DELETE_WORLD.asText(worldName));
            return;
        }

        WorldProperties worldProperties = args.<String>getOne(ArgKeys.UNLOADED_WORLD.t)
                .map(unloadedWorldName -> Sponge.getServer().getWorldProperties(unloadedWorldName))
                .orElse(args.<String>getOne(ArgKeys.DISABLED_WORLD.t).flatMap(FinderWorldProperties::getDisabledWorldProperties))
                .orElseThrow(Outputs.INVALID_UNLOADED_DISABLED_EXPORTED_WORLD_CHOICE.asSupplier());

        Sponge.getServer().deleteWorld(worldProperties).thenAccept(hasSucceed -> {
            if (hasSucceed) {
                src.sendMessage(Outputs.DELETE_WORLD.asText(worldProperties));

                try {
                    FinderFile.deleteWorldFiles(worldProperties.getUniqueId().toString());
                } catch (IOException ignored) {
                    src.sendMessage(Outputs.DELETING_WORLD_COSMOS_FILES.asText(worldProperties));
                }
            } else {
                src.sendMessage(Outputs.DELETING_WORLD.asText(worldProperties));
            }
        });
    }
}


