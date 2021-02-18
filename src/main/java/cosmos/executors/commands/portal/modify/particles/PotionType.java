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
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.potion.PotionEffectType;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class PotionType extends AbstractParticlesModifyCommand {

    public PotionType() {
        super(
                Parameter.registryElement(TypeToken.get(PotionEffectType.class), RegistryTypes.POTION_EFFECT_TYPE, ResourceKey.MINECRAFT_NAMESPACE)
                        .key(CosmosKeys.POTION_EFFECT_TYPE)
                        .optional()
                        .build()
        );
    }

    @Override
    protected ParticleEffect newParticles(final Audience src, final CommandContext context, final CosmosPortal portal, final ParticleEffect particles) throws CommandException {
        // TODO https://github.com/SpongePowered/SpongeAPI/issues/2319
        final PotionEffectType potionEffectType = context.one(CosmosKeys.POTION_EFFECT_TYPE)
                .map(value -> {
                    super.formattedModifiedValue = value.key(RegistryTypes.POTION_EFFECT_TYPE);
                    return value;
                })
                .orElse(null);

        return super.rebuildParticles(particles, ParticleOptions.POTION_EFFECT_TYPE.get(), potionEffectType);
    }

}
