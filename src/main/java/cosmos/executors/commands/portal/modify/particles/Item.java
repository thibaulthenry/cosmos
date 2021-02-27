package cosmos.executors.commands.portal.modify.particles;

import com.google.inject.Singleton;
import cosmos.executors.parameters.CosmosKeys;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.command.parameter.Parameter;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleOptions;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class Item extends AbstractParticlesModifyCommand {

    public Item() {
        super(
                Parameter.registryElement(TypeToken.get(ItemType.class), RegistryTypes.ITEM_TYPE)
                        .setKey(CosmosKeys.ITEM_TYPE)
                        .build()
        );
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final ItemStackSnapshot itemStackSnapshot = context.getOne(CosmosKeys.ITEM_TYPE)
                .map(itemType -> ItemStack.builder().itemType(itemType).build().createSnapshot())
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.ITEM_TYPE));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.ITEM_STACK_SNAPSHOT, itemStackSnapshot).build();
    }

}
