package cosmos.commands.management.properties;

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
import org.spongepowered.api.world.storage.WorldProperties;

public class Weather extends AbstractCommand {

    public Weather() {
        super(Permission.COMMAND_MANAGEMENT_PROPERTIES_WEATHER);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));

        WorldProperties worldProperties = Basics.getWorldProperties(worldName);

        args.<Long>getOne("world-time").ifPresent(worldProperties::setWorldTime);
        args.<Boolean>getOne("raining").ifPresent(worldProperties::setRaining);
        args.<Integer>getOne("raining-time").ifPresent(worldProperties::setRainTime);
        args.<Boolean>getOne("thundering").ifPresent(worldProperties::setThundering);
        args.<Integer>getOne("thundering-time").ifPresent(worldProperties::setThunderTime);

        if (Sponge.getServer().saveWorldProperties(worldProperties)) {
            src.sendMessage(Text.of(TextColors.GREEN, worldName, "'s properties has been changed successfully"));
        } else {
            throw new CommandException(Text.of("An error occurred while changing ", worldName, "'s properties"));
        }

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
                        GenericArguments.flags()
                                .valueFlag(GenericArguments.bool(Text.of("raining")), "-raining")
                                .valueFlag(GenericArguments.integer(Text.of("raining-time")), "-raining-time")
                                .valueFlag(GenericArguments.bool(Text.of("thundering")), "-thundering")
                                .valueFlag(GenericArguments.integer(Text.of("thundering-time")), "-thundering-time")
                                .buildWith(GenericArguments.none())
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
