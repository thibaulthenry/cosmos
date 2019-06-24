package cosmos.commands.management;

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
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.WorldArchetype;
import org.spongepowered.api.world.storage.WorldProperties;

import java.io.IOException;
import java.time.Instant;

public class Import extends AbstractCommand {

    public Import() {
        super(Permission.COMMAND_MANAGEMENT_IMPORT);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));

        WorldArchetype.Builder worldArchetypeBuilder = WorldArchetype.builder()
                .enabled(true)
                .generateSpawnOnLoad(true)
                .keepsSpawnLoaded(true)
                .loadsOnStartup(true);

        WorldArchetype worldArchetype = worldArchetypeBuilder.build(worldName + Instant.now(), worldName);

        WorldProperties worldProperties;
        try {
            worldProperties = Sponge.getServer().createWorldProperties(worldName, worldArchetype);
        } catch (IOException e) {
            logError(e, "An error occurred while creating ", worldName, "'s properties");
            throw new CommandException(Text.of("An error occurred while creating ", worldName, "'s properties"), e);
        }

        if (Sponge.getServer().saveWorldProperties(worldProperties)) {
            src.sendMessage(Text.of(TextColors.GREEN, worldName, " has been imported successfully"));
        } else {
            throw new CommandException(Text.of("An error occurred while saving ", worldName, "'s properties"));
        }

        return CommandResult.success();
    }

    @Override
    public CommandSpec getCommandSpec() {
        return CommandSpec.builder()
                .arguments(
                        GenericArguments.choices(Text.of("world-name"),
                                () -> Finder.toMap(Finder.getAvailableWorldNames(false)).keySet(),
                                key -> Finder.toMap(Finder.getAvailableWorldNames(false)).get(key)
                        )
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
