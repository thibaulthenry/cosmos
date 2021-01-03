package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.data.persistence.DataSerializable;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.crafting.CraftingGridInventory;

public class ExtendedInventoryData implements DataSerializable, ShareableSerializable<ServerPlayer> {

    private final InventoryData enderChestInventoryData;
    private final InventoryData playerInventoryData;
    private InventoryData craftingInventoryData;
    private ItemStack pickedItem;

    public ExtendedInventoryData(final ServerPlayer player) {
        this.enderChestInventoryData = new InventoryData(player.getEnderChestInventory());
        this.playerInventoryData = new InventoryData(player.getInventory());

        if (!player.isViewingInventory() || !player.getOpenInventory().isPresent()) {
            return;
        }

        final Container playerContainer = player.getOpenInventory().get();

        this.craftingInventoryData = playerContainer.getViewed()
                .stream()
                .filter(inventory -> inventory instanceof CraftingGridInventory)
                .findFirst()
                .map(InventoryData::new)
                .orElse(null);

        this.pickedItem = playerContainer.getCursor().orElse(null);
    }

    public ExtendedInventoryData(final InventoryData playerInventoryData, final InventoryData enderChestInventoryData, final InventoryData craftingInventoryData, final ItemStack pickedItem) {
        this.craftingInventoryData = craftingInventoryData;
        this.enderChestInventoryData = enderChestInventoryData;
        this.playerInventoryData = playerInventoryData;
        this.pickedItem = pickedItem;
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public void offer(final ServerPlayer data) {
        if (this.playerInventoryData != null) {
            data.getInventory().clear();
            this.playerInventoryData.offer(data.getInventory());
        }

        if (this.enderChestInventoryData != null) {
            data.getEnderChestInventory().clear();
            this.enderChestInventoryData.offer(data.getEnderChestInventory());
        }

        if (!data.isViewingInventory() || !data.getOpenInventory().isPresent()) {
            return;
        }

        final Container playerContainer = data.getOpenInventory().get();

        if (this.craftingInventoryData != null) {
            playerContainer.getViewed()
                    .stream()
                    .filter(inventory -> inventory instanceof CraftingGridInventory)
                    .findFirst()
                    .ifPresent(craftingInventory -> {
                        craftingInventory.clear();
                        this.craftingInventoryData.offer(craftingInventory);
                    });
        }

        if (this.pickedItem != null) {
            // TODO BUG playerContainer.setCursor(this.pickedItem);
        }
    }

    @Override
    public DataContainer toContainer() {
        final DataContainer dataContainer = DataContainer.createNew();

        if (this.playerInventoryData != null) {
            dataContainer.set(Queries.Inventories.Extended.PLAYER_INVENTORY, this.playerInventoryData);
        }

        if (this.enderChestInventoryData != null) {
            dataContainer.set(Queries.Inventories.Extended.ENDER_CHEST_INVENTORY, this.enderChestInventoryData);
        }

        if (this.craftingInventoryData != null) {
            dataContainer.set(Queries.Inventories.Extended.CRAFTING_INVENTORY, this.craftingInventoryData);
        }

        if (this.pickedItem != null && !this.pickedItem.isEmpty()) {
            dataContainer.set(Queries.Inventories.Extended.PICKED_ITEM, this.pickedItem);
        }

        return dataContainer;
    }
}
