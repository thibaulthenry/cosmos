package cosmos.executors.commands.properties;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.world.server.ServerWorldProperties;
import org.spongepowered.math.vector.Vector3d;

import java.util.Optional;

@Singleton
public class SpawnPosition extends AbstractPropertiesCommand {

    public SpawnPosition() {
        super(Parameter.vector3d().setKey(CosmosKeys.XYZ).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties) throws CommandException {
        final Optional<Vector3d> optionalInput = context.getOne(CosmosKeys.XYZ);
        Vector3d value = properties.getSpawnPosition().toDouble();

        if (optionalInput.isPresent()) {
            value = optionalInput.get().floor();
            properties.setSpawnPosition(value.toInt());
            this.serviceProvider.worldProperties().save(properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.properties.spawn-position.set" : "success.properties.spawn-position.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
