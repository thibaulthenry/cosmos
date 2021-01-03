package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class DamageAmount extends AbstractBorderCommand {

    public DamageAmount() {
        super(Parameter.doubleNumber().setKey(CosmosKeys.AMOUNT_DOUBLE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Double> optionalInput = context.getOne(CosmosKeys.AMOUNT_DOUBLE);
        double value = border.getDamageAmount();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            border.setDamageAmount(value);
            this.serviceProvider.world().saveProperties(src, properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.damage-amount.set" : "success.border.damage-amount.get")
                .replace("world", properties)
                .replace("value", value)
                .green()
                .sendTo(src);
    }
}
