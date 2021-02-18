package cosmos.executors.commands.portal.modify.particles;

import com.google.common.base.CaseFormat;
import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.registries.portal.CosmosPortal;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;

@Singleton
public class Direction extends AbstractParticlesModifyCommand {

    public Direction() {
        super(Parameter.enumValue(org.spongepowered.api.util.Direction.class).key(CosmosKeys.DIRECTION).optional().build());
    }

    @Override
    protected ParticleEffect newParticles(final Audience src, final CommandContext context, final CosmosPortal portal, final ParticleEffect particles) throws CommandException {
        final org.spongepowered.api.util.Direction direction = context.one(CosmosKeys.DIRECTION)
                .map(value -> {
                    super.formattedModifiedValue = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, value.name());
                    return value;
                })
                .orElse(null);

        return super.rebuildParticles(particles, ParticleOptions.DIRECTION.get(), direction);
    }

}
