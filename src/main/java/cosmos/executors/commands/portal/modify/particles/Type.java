package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Type extends AbstractParticlesModifyCommand {

    public Type() {
        super(
                Parameter.registryElement(TypeToken.get(ParticleType.class), RegistryTypes.PARTICLE_TYPE)
                        .setKey(CosmosKeys.PARTICLE_TYPE)
                        .build()
        );
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final ParticleType particleType = context.getOne(CosmosKeys.PARTICLE_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.PARTICLE_TYPE));

        return ParticleEffect.builder().from(particleEffect).type(particleType).build();
    }

}
