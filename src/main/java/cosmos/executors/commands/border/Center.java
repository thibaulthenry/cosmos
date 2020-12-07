package cosmos.executors.commands.border;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.world.WorldBorder;
import org.spongepowered.api.world.storage.WorldProperties;
import org.spongepowered.math.vector.Vector2d;

import java.util.Optional;

@Singleton
public class Center extends AbstractBorderCommand {

    @Inject
    public Center() {
        super(CosmosParameters.POSITION_XZ);
    }

    @Override
    protected void run(final Audience audience, final CommandContext context, final WorldProperties properties, final WorldBorder border) throws CommandException {
        final Optional<Vector2d> optionalCenter = context.getOne(CosmosParameters.POSITION_XZ);
        Vector2d center = border.getCenter().toVector2(true);

        if (optionalCenter.isPresent()) {
            center = optionalCenter.get();
            border.setCenter(center.getX(), center.getY());
            //this.serviceProvider.properties().save(properties); TODO Add in 1.16
        }

        this.serviceProvider.message().getMessage(audience, "success.border.center." + (optionalCenter.isPresent() ? "set" : "get"))
                .replace("world", properties)
                .replace("center", center)
                .successColor()
                .sendTo(audience);
    }
}
