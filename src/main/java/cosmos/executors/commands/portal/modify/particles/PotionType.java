package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
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
                Parameter.registryElement(TypeToken.get(PotionEffectType.class), RegistryTypes.POTION_EFFECT_TYPE)
                        .setKey(CosmosKeys.POTION_EFFECT_TYPE)
                        .build()
        );
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final PotionEffectType potionEffectType = context.getOne(CosmosKeys.POTION_EFFECT_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.POTION_EFFECT_TYPE));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.POTION_EFFECT_TYPE, potionEffectType).build();
    }

}
