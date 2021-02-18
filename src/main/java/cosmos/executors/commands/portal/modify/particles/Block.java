package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.registries.portal.CosmosPortal;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Block extends AbstractParticlesModifyCommand {

    public Block() {
        super(
                Parameter.registryElement(TypeToken.get(BlockType.class), RegistryTypes.BLOCK_TYPE, ResourceKey.MINECRAFT_NAMESPACE)
                        .key(CosmosKeys.BLOCK_TYPE)
                        .optional()
                        .build()
        );
    }

    @Override
    protected ParticleEffect newParticles(final Audience src, final CommandContext context, final CosmosPortal portal, final ParticleEffect particles) throws CommandException {
        if (!super.isAnyOf(particles.type(), ParticleTypes.BLOCK.get(), ParticleTypes.FALLING_DUST.get(), ParticleTypes.ITEM.get())) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.portal.particles.type")
                    .replace("prop", ParticleOptions.BLOCK_STATE.get().key(RegistryTypes.PARTICLE_OPTION))
                    .replace("type", particles.type().key(RegistryTypes.PARTICLE_TYPE))
                    .asError();
        }

        final BlockState blockState = context.one(CosmosKeys.BLOCK_TYPE)
                .map(value -> {
                    super.formattedModifiedValue = value.key(RegistryTypes.BLOCK_TYPE);
                    return BlockState.builder().blockType(value).build();
                })
                .orElse(null);

        return super.rebuildParticles(particles, ParticleOptions.BLOCK_STATE.get(), blockState);
    }

}
