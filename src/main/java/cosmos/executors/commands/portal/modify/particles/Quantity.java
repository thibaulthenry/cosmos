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
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Quantity extends AbstractParticlesModifyCommand {

    public Quantity() {
        super(Parameter.rangedInteger(1, 1000).key(CosmosKeys.AMOUNT).optional().build());
    }

    @Override
    protected ParticleEffect newParticles(final Audience src, final CommandContext context, final CosmosPortal portal, final ParticleEffect particles) throws CommandException {
        if (super.isAnyOf(particles.type(), ParticleTypes.BLOCK.get(), ParticleTypes.FIREWORK.get())) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.portal.particles.type")
                    .replace("prop", ParticleOptions.QUANTITY.get().key(RegistryTypes.PARTICLE_OPTION))
                    .replace("type", particles.type().key(RegistryTypes.PARTICLE_TYPE))
                    .asError();
        }

        final Integer quantity = context.one(CosmosKeys.AMOUNT)
                .map(value -> {
                    super.formattedModifiedValue = value;
                    return value;
                })
                .orElse(null);

        return super.rebuildParticles(particles, ParticleOptions.QUANTITY.get(), quantity);
    }

}
