package cosmos.executors.commands.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.portal.PortalAll;
import cosmos.executors.parameters.impl.world.WorldOnline;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.util.Ticks;
import org.spongepowered.math.vector.Vector3d;

import java.time.temporal.ChronoUnit;

@Singleton
public class Cooldown extends AbstractPortalModifyCommand {

    @Inject
    public Cooldown(final Injector injector) {
        super(
                injector.getInstance(PortalAll.class).key(CosmosKeys.PORTAL_COSMOS).build(),
                CosmosParameters.DURATION_WITH_TIME_UNIT
        );
    }

    @Override
    protected CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final long duration = context.getOne(CosmosKeys.DURATION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "parameter", CosmosKeys.DURATION));

        final ChronoUnit unit = context.getOne(CosmosKeys.TIME_UNIT).orElse(ChronoUnit.SECONDS);

        return portal.asBuilder().cooldown(Ticks.zero()).build(); // todo
    }

}
