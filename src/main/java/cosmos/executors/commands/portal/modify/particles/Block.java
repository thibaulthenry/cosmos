package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import cosmos.executors.parameters.CosmosParameters;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;

@Singleton
public class Block extends AbstractParticlesModifyCommand {

    @Inject
    public Block() {
        super(CosmosParameters.BLOCK_TYPE);
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final BlockState blockState = context.getOne(CosmosKeys.BLOCK_TYPE)
                .map(blockType -> BlockState.builder().blockType(blockType).build())
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.BLOCK_TYPE));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.BLOCK_STATE, blockState).build();
    }

}
