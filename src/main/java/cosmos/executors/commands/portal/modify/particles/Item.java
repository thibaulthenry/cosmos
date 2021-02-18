package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.constants.CosmosKeys;
import cosmos.registries.portal.CosmosPortal;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Item extends AbstractParticlesModifyCommand {

    public Item() {
        super(
                Parameter.registryElement(TypeToken.get(ItemType.class), RegistryTypes.ITEM_TYPE, ResourceKey.MINECRAFT_NAMESPACE)
                        .key(CosmosKeys.ITEM_TYPE)
                        .optional()
                        .build()
        );
    }

    @Override
    protected ParticleEffect newParticles(final Audience src, final CommandContext context, final CosmosPortal portal, final ParticleEffect particles) throws CommandException {
        if (!super.isAnyOf(particles.type(), ParticleTypes.BLOCK.get(), ParticleTypes.FALLING_DUST.get(), ParticleTypes.ITEM.get())) {
            throw super.serviceProvider.message()
                    .getMessage(src, "error.invalid.portal.particles.type")
                    .replace("prop", ParticleOptions.ITEM_STACK_SNAPSHOT.get().key(RegistryTypes.PARTICLE_OPTION))
                    .replace("type", particles.type().key(RegistryTypes.PARTICLE_TYPE))
                    .asError();
        }

        final ItemStackSnapshot itemStackSnapshot = context.one(CosmosKeys.ITEM_TYPE)
                .map(value -> {
                    super.formattedModifiedValue = value.key(RegistryTypes.ITEM_TYPE);
                    return ItemStack.builder().itemType(value).build().createSnapshot();
                })
                .orElse(null);

        return super.rebuildParticles(particles, ParticleOptions.ITEM_STACK_SNAPSHOT.get(), itemStackSnapshot);
    }

}
