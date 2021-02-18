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
public class ViewDistance extends AbstractPropertiesCommand {

    public ViewDistance() {
        super(Parameter.rangedInteger(3, 32).key(CosmosKeys.CHUNKS).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<Integer> optionalInput = context.one(CosmosKeys.CHUNKS);
        int value = properties.viewDistance();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setViewDistance(value);
            super.serviceProvider.world().saveProperties(src, properties);
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.view-distance.set" : "success.properties.view-distance.get")
                .replace("value", value)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
