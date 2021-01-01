package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class DamageThreshold extends AbstractBorderCommand {

    public DamageThreshold() {
        super(Parameter.doubleNumber().setKey(CosmosKeys.DISTANCE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Double> optionalInput = context.getOne(CosmosKeys.DISTANCE);
        double value = border.getDamageThreshold();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            border.setDamageThreshold(value);
            this.serviceProvider.world().saveProperties(src, properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.damage-threshold.set" : "success.border.damage-threshold.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
