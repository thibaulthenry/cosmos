package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.registries.portal.CosmosPortal;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
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
                Parameter.registryElement(TypeToken.get(ParticleType.class), RegistryTypes.PARTICLE_TYPE, ResourceKey.MINECRAFT_NAMESPACE)
                        .key(CosmosKeys.PARTICLE_TYPE)
                        .optional()
                        .build()
        );
    }

    @Override
    protected ParticleEffect newParticles(final Audience src, final CommandContext context, final CosmosPortal portal, final ParticleEffect particles) throws CommandException {
        return context.one(CosmosKeys.PARTICLE_TYPE)
                .map(value -> {
                    super.formattedModifiedValue = value.key(RegistryTypes.PARTICLE_TYPE);
                    return ParticleEffect.builder().from(particles).type(value).build();
                })
                .orElse(null);
    }

}
