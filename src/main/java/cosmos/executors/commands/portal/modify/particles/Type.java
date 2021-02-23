package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.commands.AbstractCommand;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Type extends AbstractParticlesModifyCommand {

    @Inject
    public Type() {
        super(CosmosParameters.PARTICLE_TYPE);
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final ParticleType particleType = context.getOne(CosmosKeys.PARTICLE_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PARTICLE_TYPE));

        return ParticleEffect.builder().from(particleEffect).type(particleType).build();
    }

}
