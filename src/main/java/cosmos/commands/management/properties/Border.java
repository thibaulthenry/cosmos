package cosmos.commands.management.properties;

import com.flowpowered.math.vector.Vector3d;
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
import org.spongepowered.api.world.SerializationBehavior;
import org.spongepowered.api.world.storage.WorldProperties;

public class Border extends AbstractCommand {

    public Border() {
        super(Permission.COMMAND_MANAGEMENT_PROPERTIES_BORDER);
    }

    @Override
    protected CommandResult run(CommandSource src, CommandContext args) throws CommandException {
        String worldName = args.<String>getOne("world-name")
                .orElseThrow(supplyError("Please insert a valid world name"));

        WorldProperties worldProperties = Basics.getWorldProperties(worldName);

        args.<Vector3d>getOne("world-border-center").ifPresent(worldBorderCenter ->
                worldProperties.setWorldBorderCenter(worldBorderCenter.getX(), worldBorderCenter.getZ())
        );
        args.<Double>getOne("world-border-diameter").ifPresent(worldProperties::setWorldBorderDiameter);
        args.<Long>getOne("world-border-time-remaining").ifPresent(worldProperties::setWorldBorderTimeRemaining);
        args.<Double>getOne("world-border-target-diameter").ifPresent(worldProperties::setWorldBorderTargetDiameter);
        args.<Double>getOne("world-border-damage-threshold").ifPresent(worldProperties::setWorldBorderDamageThreshold);
        args.<Double>getOne("world-border-damage-amount").ifPresent(worldProperties::setWorldBorderDamageAmount);
        args.<Integer>getOne("world-border-warning-time").ifPresent(worldProperties::setWorldBorderWarningTime);
        args.<Integer>getOne("world-border-warning-distance").ifPresent(worldProperties::setWorldBorderWarningDistance);
        args.<SerializationBehavior>getOne("serialization-behavior").ifPresent(worldProperties::setSerializationBehavior);

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
                                .valueFlag(GenericArguments.vector3d(Text.of("world-border-center")), "-world-border-center")
                                .valueFlag(GenericArguments.doubleNum(Text.of("world-border-diameter")), "-world-border-diameter")
                                .valueFlag(GenericArguments.longNum(Text.of("world-border-time-remaining")), "-world-border-time-remaining")
                                .valueFlag(GenericArguments.doubleNum(Text.of("world-border-target-diameter")), "-world-border-target-diameter")
                                .valueFlag(GenericArguments.doubleNum(Text.of("world-border-damage-threshold")), "-world-border-damage-threshold")
                                .valueFlag(GenericArguments.doubleNum(Text.of("world-border-damage-amount")), "-world-border-damage-amount")
                                .valueFlag(GenericArguments.integer(Text.of("world-border-warning-time")), "-world-border-warning-time")
                                .valueFlag(GenericArguments.integer(Text.of("world-border-warning-distance")), "-world-border-warning-distance")
                                .buildWith(GenericArguments.none())
                )
                .permission(permission)
                .executor(this)
                .build();
    }
}
