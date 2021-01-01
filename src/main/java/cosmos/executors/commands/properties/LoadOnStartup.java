package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class LoadOnStartup extends AbstractPropertiesCommand {

    public LoadOnStartup() {
        super(Parameter.bool().setKey(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<Boolean> optionalInput = context.getOne(CosmosKeys.STATE);
        boolean value = properties.doesLoadOnStartup();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setLoadOnStartup(value);
            this.serviceProvider.world().saveProperties(src, properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.load-on-startup.set" : "success.properties.load-on-startup.get")
                .replace("world", properties)
                .condition("value", value)
                .condition("tip", value)
                .successColor()
                .sendTo(src);
    }
}
