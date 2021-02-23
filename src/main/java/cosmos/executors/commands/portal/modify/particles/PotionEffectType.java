package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;

@Singleton
public class PotionEffectType extends AbstractParticlesModifyCommand {

    @Inject
    public PotionEffectType() {
        super(CosmosParameters.POTION_EFFECT_TYPE);
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final org.spongepowered.api.effect.potion.PotionEffectType potionEffectType = context.getOne(CosmosKeys.POTION_EFFECT_TYPE)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.POTION_EFFECT_TYPE));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.POTION_EFFECT_TYPE, potionEffectType).build();
    }

}
