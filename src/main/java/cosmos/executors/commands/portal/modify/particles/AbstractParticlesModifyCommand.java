package cosmos.executors.commands.portal.modify.particles;

import cosmos.executors.commands.portal.modify.AbstractPortalModifyCommand;
import cosmos.registries.portal.CosmosFramePortal;
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
    protected final CosmosFramePortal getNewPortal(final Audience src, final CommandContext context, final CosmosFramePortal portal) throws CommandException {
        final ParticleEffect particleEffect = portal.getParticles()
                .orElse(ParticleEffect.builder().type(ParticleTypes.PORTAL).build());

        return portal.asBuilder().addParticles(this.getNewParticles(src, context, particleEffect)).build();
    }

    protected abstract ParticleEffect getNewParticles(Audience src, CommandContext context, ParticleEffect particleEffect) throws CommandException;

}
