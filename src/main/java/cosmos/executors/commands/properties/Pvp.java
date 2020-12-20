package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Optional;

@Singleton
public class Pvp extends AbstractPropertiesCommand {

    public Pvp() {
        super(Parameter.bool().setKey(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties) throws CommandException {
        final Optional<Boolean> optionalInput = context.getOne(CosmosKeys.STATE);
        boolean value = properties.isPVPEnabled();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setPVPEnabled(value);
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.pvp.set" : "success.properties.pvp.get")
                .replace("world", properties)
                .condition("value", value)
                .condition("tip", value)
                .successColor()
                .sendTo(src);
    }
}
