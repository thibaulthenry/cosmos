package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.constants.CosmosParameters;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Color extends AbstractParticlesModifyCommand {

    public Color() {
        super(CosmosParameters.COLOR.get().optional().build());
    }

    @Override
    protected ParticleEffect newParticles(final Audience src, final CommandContext context, final CosmosPortal portal, final ParticleEffect particles) throws CommandException {
        if (!super.isAnyOf(particles.type(), ParticleTypes.AMBIENT_ENTITY_EFFECT.get(), ParticleTypes.DUST.get(), ParticleTypes.ENTITY_EFFECT.get())) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.portal.particles.type")
                    .replace("prop", ParticleOptions.COLOR.get().key(RegistryTypes.PARTICLE_OPTION))
                    .replace("type", particles.type().key(RegistryTypes.PARTICLE_TYPE))
                    .asError();
        }

        final org.spongepowered.api.util.Color color = context.one(CosmosKeys.COLOR)
                .map(value -> {
                    super.formattedModifiedValue = Component.text(value.toString(), value);
                    return org.spongepowered.api.util.Color.ofRgb(value.value());
                })
                .orElse(null);

        return super.rebuildParticles(particles, ParticleOptions.COLOR.get(), color);
    }

}
