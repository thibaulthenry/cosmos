package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;

@Singleton
public class Quantity extends AbstractParticlesModifyCommand {

    public Quantity() {
        super(Parameter.integerNumber().setKey(CosmosKeys.AMOUNT).build());
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final int quantity = context.getOne(CosmosKeys.AMOUNT)
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.AMOUNT));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.QUANTITY, quantity).build();
    }

}
