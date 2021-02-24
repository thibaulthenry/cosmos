package cosmos.executors.commands.portal.modify.particles;

import cosmos.executors.commands.portal.modify.AbstractPortalModifyCommand;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;

abstract class AbstractParticlesModifyCommand extends AbstractPortalModifyCommand {

    AbstractParticlesModifyCommand(final Parameter... parameters) {
        super(parameters);
    }

    @Override
    protected final CosmosPortal getNewPortal(final Audience src, final CommandContext context, final CosmosPortal portal) throws CommandException {
        final ParticleEffect particleEffect = portal.particles()
                .orElse(ParticleEffect.builder().type(ParticleTypes.PORTAL).build());

        return portal.asBuilder().particles(this.getNewParticles(src, context, particleEffect)).build();
    }

    protected abstract ParticleEffect getNewParticles(Audience src, CommandContext context, ParticleEffect particleEffect) throws CommandException;

}
