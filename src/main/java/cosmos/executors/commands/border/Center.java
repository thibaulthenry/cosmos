package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.ResourceKeyedValueParameters;
import org.spongepowered.api.world.border.WorldBorder;
import org.spongepowered.api.world.server.storage.ServerWorldProperties;
import org.spongepowered.math.vector.Vector2d;

import java.util.Optional;

@Singleton
public class Center extends AbstractBorderCommand {

    public Center() {
        super(
                Parameter.builder(Vector2d.class)
                        .addParser(ResourceKeyedValueParameters.VECTOR2D)
                        .key(CosmosKeys.X_Z)
                        .optional()
                        .build()
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Vector2d> optionalInput = context.one(CosmosKeys.X_Z);
        Vector2d value = border.center();

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            // todo border.toBuilder().center(value.x(), value.y()).build();
            super.serviceProvider.world().saveProperties(src, properties);
        }

        super.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.center.set" : "success.border.center.get")
                .replace("value", value)
                .replace("world", properties)
                .green()
                .sendTo(src);
    }

}
