package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.item.inventory.Inventory;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryData implements ShareableSerializable<Inventory> {

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

    public InventoryData() {
        this.slots = null;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public void share(final Inventory data) {
        data.clear();

        if (this.slots == null) {
            return;
        }

        this.slots.forEach(inventorySlotData -> inventorySlotData.share(data));
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
