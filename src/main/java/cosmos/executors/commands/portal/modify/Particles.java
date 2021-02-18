package cosmos.executors.commands.portal.modify;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import cosmos.executors.parameters.impl.portal.PortalAll;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.util.Direction;
import org.spongepowered.math.vector.Vector3d;

@Singleton
public class Particles extends AbstractCommand {

    @Inject
    public Particles(final Injector injector) {
        super(
                injector.getInstance(PortalAll.class).key(CosmosKeys.PORTAL_COSMOS).build(),
                CosmosParameters.PARTICLE_TYPE
        );
    }

    @Override
    protected void run(final Audience src, final CommandContext context) throws CommandException {
        final CosmosPortal portal = context.getOne(CosmosKeys.PORTAL_COSMOS)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PORTAL_COSMOS));

        final ParticleType particleType = context.getOne(CosmosKeys.PARTICLE_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PARTICLE_TYPE));

        final ParticleEffect particleEffect = ParticleEffect.builder()
                .type(particleType)
                .option(ParticleOptions.OFFSET.get(), Vector3d.ZERO)
               // .option(ParticleOptions.VELOCITY.get(), Vector3d.from(0.02, 0.1, 0.03))
                .option(ParticleOptions.QUANTITY.get(), 3)
                .build();

        super.serviceProvider.registry().portal().replace(portal.asBuilder().addParticles(particleEffect).build());
    }

}
