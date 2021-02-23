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
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

@Singleton
public class Item extends AbstractParticlesModifyCommand {

    @Inject
    public Item() {
        super(CosmosParameters.ITEM_TYPE);
    }

    @Override
    protected ParticleEffect getNewParticles(final Audience src, final CommandContext context, final ParticleEffect particleEffect) throws CommandException {
        final ItemStackSnapshot itemStackSnapshot = context.getOne(CosmosKeys.ITEM_TYPE)
                .map(itemType -> ItemStack.builder().itemType(itemType).build().createSnapshot())
                .orElseThrow(super.serviceProvider.message().supplyError(src, "error.invalid.value", "param", CosmosKeys.ITEM_TYPE));

        return ParticleEffect.builder().from(particleEffect).option(ParticleOptions.ITEM_STACK_SNAPSHOT, itemStackSnapshot).build();
    }

}
