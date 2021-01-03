package cosmos.registries.data.builder.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.impl.InventorySlotData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataView;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

@Singleton
public class InventorySlotDataBuilder extends AbstractDataBuilder<InventorySlotData> {

    @Inject
    public InventorySlotDataBuilder() {
        this(InventorySlotData.class, 1);
    }

    protected InventorySlotDataBuilder(final Class<InventorySlotData> requiredClass, final int supportedVersion) {
        super(requiredClass, supportedVersion);
    }

    @Override
    protected Optional<InventorySlotData> buildContent(final DataView container) throws InvalidDataException {
        if (!container.contains(Queries.Inventories.SLOT_INDEX, Queries.Inventories.SLOT_STACK)) {
            return Optional.empty();
        }

        final int index = container.getInt(Queries.Inventories.SLOT_INDEX)
                .orElseThrow(() -> new InvalidDataException("Missing slot index while building InventorySlotData"));

        final ItemStack itemStack = container.getView(Queries.Inventories.SLOT_STACK)
                .flatMap(view -> Sponge.getDataManager().deserialize(ItemStack.class, view))
                .orElseThrow(() -> new InvalidDataException("Missing slot stack while building InventorySlotData"));

        return Optional.of(new InventorySlotData(index, itemStack));
    }

}
