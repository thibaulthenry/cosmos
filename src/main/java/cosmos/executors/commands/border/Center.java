package cosmos.executors.commands.border;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.command.parameter.managed.standard.CatalogedValueParameters;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.math.vector.Vector2d;

import java.util.Optional;

@Singleton
public class Center extends AbstractBorderCommand {

    public Center() {
        super(Parameter.builder(Vector2d.class, CatalogedValueParameters.VECTOR2D).setKey(CosmosKeys.XZ).optional().build());
    }

    @Override
    protected void run(final Audience src, final CommandContext context, final WorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Vector2d> optionalInput = context.getOne(CosmosKeys.XZ);
        Vector2d value = border.getCenter().toVector2(true);

        if (optionalInput.isPresent()) {
            value = optionalInput.get();
            border.setCenter(value.getX(), value.getY());
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message()
                .getMessage(src, optionalInput.isPresent() ? "success.border.center.set" : "success.border.center.get")
                .replace("world", properties)
                .replace("value", value)
                .successColor()
                .sendTo(src);
    }
}
