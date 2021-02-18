package cosmos.executors.commands.portal.modify.particles;

import cosmos.executors.commands.portal.modify.AbstractPortalModifyCommand;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOption;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.registry.RegistryTypes;

import java.util.Arrays;

abstract class AbstractParticlesModifyCommand extends AbstractPortalModifyCommand {

    AbstractParticlesModifyCommand(final Parameter... parameters) {
        super(parameters);
    }

    protected boolean isAnyOf(final ParticleType target, final ParticleType... particleTypes) {
        return Arrays.asList(particleTypes).contains(target);
    }

    @Override
    protected final CosmosPortal newPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final ParticleEffect particleEffect = portal.particles()
                .orElse(ParticleEffect.builder().type(ParticleTypes.PORTAL).build());

        return portal.asBuilder().particles(this.newParticles(src, context, portal, particleEffect)).build();
    }

    @Override
    protected String propertyPrefix() {
        return "particles";
    }

    @SuppressWarnings("unchecked")
    protected <V> ParticleEffect rebuildParticles(final ParticleEffect particles, final ParticleOption<V> inputOption, @Nullable final V inputValue) {
        final ParticleEffect.Builder builder = ParticleEffect.builder().type(particles.type());

        RegistryTypes.PARTICLE_OPTION.get().streamEntries().forEach(entry -> {
            final ParticleOption<?> option = entry.value();

            if (!entry.key().equals(inputOption.key(RegistryTypes.PARTICLE_OPTION))) {
                particles.option(option).ifPresent(value -> builder.option((ParticleOption<V>) option, (V) value));
            } else if (inputValue != null) {
                builder.option(inputOption, inputValue);
            }
        });

        return builder.build();
    }

    protected abstract ParticleEffect newParticles(Audience src, CommandContext context, CosmosPortal portal, ParticleEffect particles) throws CommandException;

}
