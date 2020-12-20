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
public class ViewDistance extends AbstractPropertiesCommand {

    public ViewDistance() {
        super(Parameter.rangedInteger(3, 32).setKey(CosmosKeys.CHUNKS).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties) throws CommandException {
        final Optional<Integer> optionalInput = context.getOne(CosmosKeys.CHUNKS);
        int value = properties.getViewDistance();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            properties.setViewDistance(value);
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.view-distance.set" : "success.properties.view-distance.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
