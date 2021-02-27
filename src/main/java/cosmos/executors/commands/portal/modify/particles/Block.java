package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Block extends AbstractParticlesModifyCommand {

    public Block() {
        super(Parameter.registryElement(TypeToken.get(BlockType.class), RegistryTypes.BLOCK_TYPE).setKey(CosmosKeys.BLOCK_TYPE).build());
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final BlockState blockState = context.getOne(CosmosKeys.BLOCK_TYPE)
                .map(blockType -> BlockState.builder().blockType(blockType).build())
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.BLOCK_TYPE));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.BLOCK_STATE, blockState).build();
    }

}
