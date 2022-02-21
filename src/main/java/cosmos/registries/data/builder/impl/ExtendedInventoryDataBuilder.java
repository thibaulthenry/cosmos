package cosmos.registries.data.builder.impl;


import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.ExtendedInventoryData;
import cosmos.registries.data.serializable.impl.InventoryData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

@Singleton
public class ExtendedInventoryDataBuilder extends AbstractDataBuilder<ExtendedInventoryData> {

    @Inject
    public ExtendedInventoryDataBuilder() {
        this(ExtendedInventoryData.class, 1);
    }

    protected ExtendedInventoryDataBuilder(final Class<ExtendedInventoryData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<ExtendedInventoryData> buildContent(final DataView container) throws InvalidDataException {
        final InventoryData playerInventoryData = container.getView(Queries.Inventory.Extended.PLAYER_INVENTORY)
                .flatMap(view -> Sponge.dataManager().deserialize(InventoryData.class, view))
                .orElse(null);

        final InventoryData enderChestInventoryData = container.getView(Queries.Inventory.Extended.ENDER_CHEST_INVENTORY)
                .flatMap(view -> Sponge.dataManager().deserialize(InventoryData.class, view))
                .orElse(null);

        final InventoryData craftingInventoryData = container.getView(Queries.Inventory.Extended.CRAFTING_INVENTORY)
                .flatMap(view -> Sponge.dataManager().deserialize(InventoryData.class, view))
                .orElse(null);

        final ItemStack pickedItem = container.getView(Queries.Inventory.Extended.PICKED_ITEM)
                .flatMap(view -> Sponge.dataManager().deserialize(ItemStack.class, view))
                .orElse(null);

        return Optional.of(new ExtendedInventoryData(playerInventoryData, enderChestInventoryData, craftingInventoryData, pickedItem));
    }

}
