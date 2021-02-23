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
public class Direction extends AbstractParticlesModifyCommand {

    @Inject
    public Direction() {
        super(CosmosParameters.DIRECTION);
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final org.spongepowered.api.util.Direction direction = context.getOne(CosmosKeys.DIRECTION)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.DIRECTION));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.DIRECTION, direction).build();
    }

}
