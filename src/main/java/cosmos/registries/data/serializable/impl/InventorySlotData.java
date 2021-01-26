package cosmos.registries.data.serializable.impl;

import cosmos.Cosmos;
import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.Keys;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;

public class InventorySlotData implements ShareableSerializable<Inventory> {

    private final int slotIndex;
    private final ItemStack slotStack;

    public InventorySlotData(final Slot slot) {
        this.slotIndex = slot.get(Keys.SLOT_INDEX).orElse(-1);
        this.slotStack = slot.peek();
    }

    public InventorySlotData(final int slotIndex, final ItemStack slotStack) {
        this.slotIndex = slotIndex;
        this.slotStack = slotStack;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    public boolean isPresent() {
        return !this.slotStack.isEmpty();
    }

    @Override
    public void share(final Inventory data) {
        if (this.slotIndex < 0 || this.slotStack == null || this.slotStack.isEmpty()) {
            return;
        }

        final int fail = data.set(this.slotIndex, this.slotStack).getRejectedItems().size();

        // todo

        if (fail > 0) {
            Cosmos.getLogger().warn("Fail for " + fail + " items");
        }
    }

    @Override
    public DataContainer toContainer() {
        return DataContainer.createNew()
                .set(Queries.Inventories.SLOT_INDEX, this.slotIndex)
                .set(Queries.Inventories.SLOT_STACK, this.slotStack);
    }

}
