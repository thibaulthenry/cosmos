package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryData implements DataSerializable, ShareableSerializable<Inventory> {

    private final List<InventorySlotData> slots;

    public InventoryData(final Inventory inventory) {
        this.slots = inventory.slots()
                .stream()
                .map(InventorySlotData::new)
                .collect(Collectors.toList());
    }

    public InventoryData(final List<InventorySlotData> slots) {
        this.slots = slots;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public void offer(final Inventory data) {
        if (this.slots == null) {
            return;
        }

        this.slots.forEach(inventorySlotData -> inventorySlotData.offer(data));
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew().set(
                Queries.Inventories.INVENTORY,
                this.slots.stream()
                        .filter(InventorySlotData::isPresent)
                        .map(InventorySlotData::toContainer)
                        .collect(Collectors.toList())
        );
    }
}
