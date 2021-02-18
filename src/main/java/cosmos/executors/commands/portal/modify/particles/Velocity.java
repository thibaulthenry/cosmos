package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.math.vector.Vector3d;

@Singleton
public class Velocity extends AbstractParticlesModifyCommand {

    public Velocity() {
        super(Parameter.vector3d().key(CosmosKeys.X_Y_Z).optional().build());
    }

    @Override
    protected ParticleEffect newParticles(final Audience src, final CommandContext context, final CosmosPortal portal, final ParticleEffect particles) throws CommandException {
        final Vector3d velocity = context.one(CosmosKeys.X_Y_Z)
                .map(value -> {
                    super.formattedModifiedValue = value;
                    return value;
                })
                .orElse(null);

        return super.rebuildParticles(particles, ParticleOptions.VELOCITY.get(), velocity);
    }

}
