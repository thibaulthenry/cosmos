package cosmos.registries.data.serializable.impl;

import cosmos.constants.Queries;
import cosmos.registries.data.serializable.ShareableSerializable;
import org.spongepowered.api.data.persistence.DataContainer;
import org.spongepowered.api.entity.living.player.server.ServerPlayer;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.crafting.CraftingGridInventory;

import java.util.Optional;

public class ExtendedInventoryData implements ShareableSerializable<ServerPlayer> {

    private final InventoryData craftingInventoryData;
    private final InventoryData enderChestInventoryData;
    private final InventoryData playerInventoryData;
    private final ItemStack pickedItem;

    public ExtendedInventoryData(final ServerPlayer player) {
        this.enderChestInventoryData = new InventoryData(player.enderChestInventory());
        this.playerInventoryData = new InventoryData(player.inventory());

        if (!player.isViewingInventory() || !player.openInventory().isPresent()) {
            this.craftingInventoryData = null;
            this.pickedItem = null;

            return;
        }

        final Container playerContainer = player.openInventory().get();

        this.craftingInventoryData = playerContainer.viewed()
                .stream()
                .filter(inventory -> inventory instanceof CraftingGridInventory)
                .findFirst()
                .map(InventoryData::new)
                .orElse(null);

        this.pickedItem = playerContainer.cursor().orElse(null);
    }

    public ExtendedInventoryData(final InventoryData playerInventoryData, final InventoryData enderChestInventoryData, final InventoryData craftingInventoryData, final ItemStack pickedItem) {
        this.craftingInventoryData = craftingInventoryData;
        this.enderChestInventoryData = enderChestInventoryData;
        this.playerInventoryData = playerInventoryData;
        this.pickedItem = pickedItem;
    }

    public ExtendedInventoryData() {
        this.craftingInventoryData = null;
        this.enderChestInventoryData = null;
        this.playerInventoryData = null;
        this.pickedItem = null;
    }

    @Override
    public int contentVersion() {
        return 1;
    }

    @Override
    public void share(final ServerPlayer data) {
        Optional.ofNullable(this.playerInventoryData)
                .orElse(new InventoryData())
                .share(data.inventory());

        Optional.ofNullable(this.enderChestInventoryData)
                .orElse(new InventoryData())
                .share(data.enderChestInventory());

        if (!data.isViewingInventory() || !data.openInventory().isPresent()) {
            return;
        }

        final Container playerContainer = data.openInventory().get();

        if (this.pickedItem != null) {
            // playerContainer.setCursor(this.pickedItem);
            // TODO https://github.com/SpongePowered/Sponge/issues/3258
        }

        final Optional<Inventory> optionalCraftingInventory = playerContainer.viewed()
                .stream()
                .filter(inventory -> inventory instanceof CraftingGridInventory)
                .findFirst();

        if (!optionalCraftingInventory.isPresent()) {
            return;
        }

        Optional.ofNullable(this.craftingInventoryData)
                .orElse(new InventoryData())
                .share(optionalCraftingInventory.get());
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
