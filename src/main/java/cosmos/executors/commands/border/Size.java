package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@Singleton
public class Size extends AbstractBorderCommand {

    public Size() {
        super(Parameter.doubleNumber().setKey(CosmosKeys.DIAMETER).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Double> optionalInput = context.getOne(CosmosKeys.DIAMETER);
        double value = border.getDiameter();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            border.setDiameter(value);
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.size.set" : "success.border.size.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
