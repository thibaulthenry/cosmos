package cosmos.commands.management;

import cosmos.Cosmos;
import cosmos.commands.AbstractCommand;
import cosmos.utils.Finder;
import cosmos.utils.Permission;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class Duplicate extends AbstractCommand {

    public Duplicate() {
        super(Permission.COMMAND_MANAGEMENT_DUPLICATE);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String copyName = args.<String>getOne("copy-name").orElseThrow(supplyError("Please insert a valid new world name"));
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid target world name"));
        WorldProperties worldProperties = Sponge.getServer().getWorldProperties(worldName)
                .orElseThrow(supplyError("Please insert a valid target world name"));

        if (Sponge.getServer().getWorldProperties(copyName).isPresent()) {
            throw new CommandException(Text.of("A world already exists with ", copyName, " as name"));
        }

        Task.builder().async().execute(() -> {
            CompletableFuture<Optional<WorldProperties>> copyTask = Sponge.getServer().copyWorld(worldProperties, copyName);

            copyTask.thenRun(() -> {
                try {
                    if (!copyTask.get().isPresent()) {
                        src.sendMessage(Text.of(TextColors.RED, "An error occurred while duplicating ", worldName, " as ", copyName));
                    } else {
                        src.sendMessage(Text.of(TextColors.GREEN, worldName, " has been duplicated successfully as ", copyName));
                    }
                } catch (InterruptedException | ExecutionException e) {
                    logError(e, "An error occurred while duplicating ", worldName, " as ", copyName);
                    src.sendMessage(Text.of(TextColors.RED, "An error occurred while duplicating ", worldName, " as ", copyName));
                }
            });
        }).submit(Cosmos.getInstance());

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getAvailableWorldNames(true)).keySet(),
                                key -> Finder.toMap(Finder.getAvailableWorldNames(true)).get(key)
                        ),
                        GenericArguments.string(Text.of("copy-name"))
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
