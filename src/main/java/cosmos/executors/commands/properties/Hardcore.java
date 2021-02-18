package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;

import java.util.Optional;

@Singleton
public class Hardcore extends AbstractPropertiesCommand {

    public Hardcore() {
        super(Parameter.bool().key(CosmosKeys.STATE).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<Boolean> optionalInput = context.one(CosmosKeys.STATE);
        boolean value = properties.hardcore();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setHardcore(value);
            super.serviceProvider.world().saveProperties(src, properties);
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.hardcore.set" : "success.properties.hardcore.get")
                .replace("world", properties)
                .condition("value", value)
                .condition("tip", optionalInput.isPresent() && value)
                .green()
                .sendTo(src);
    }

}
