package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.models.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.server.ServerWorldProperties;
import org.spongepowered.math.vector.Vector2d;

import java.util.Optional;

@Singleton
public class Center extends AbstractBorderCommand {

    public Center() {
        super(CosmosParameters.POSITION_2D_OPTIONAL);
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final ServerWorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Vector2d> optionalInput = context.getOne(CosmosParameters.POSITION_2D_OPTIONAL);
        Vector2d value = border.getCenter().toVector2(true);

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            border.setCenter(value.getX(), value.getY());
            this.serviceProvider.world().saveProperties(src, properties);
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.center.set" : "success.border.center.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
