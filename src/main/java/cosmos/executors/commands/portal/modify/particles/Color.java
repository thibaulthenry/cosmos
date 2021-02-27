package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;

@Singleton
public class Color extends AbstractParticlesModifyCommand {

    public Color() {
        super(CosmosParameters.Builder.COLOR.get().build());
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final org.spongepowered.api.util.Color color = context.getOne(CosmosKeys.COLOR)
                .map(namedTextColor -> org.spongepowered.api.util.Color.ofRgb(namedTextColor.value()))
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.COLOR));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.COLOR, color).build();
    }

}
