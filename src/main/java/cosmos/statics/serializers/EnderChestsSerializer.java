package cosmos.statics.serializers;

import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import java.nio.file.Path;
import java.util.Collections;
import java.util.Optional;

public class EnderChestsSerializer {

    public static void serialize(Path path, Player player) {
        DataContainer dataContainer = DataContainer.createNew();

        int index = 0;
        for (Inventory slot : player.getEnderChestInventory().slots()) {
            DataQuery slotPath = DataQuery.of("EnderChestInventory", Integer.toString(index));
            slot.peek().ifPresent(itemStack -> dataContainer.set(slotPath, itemStack));
            index++;
        }

        serialize(path, dataContainer);
    }

    public static void serialize(Path path, DataContainer dataContainer) {
        DataQuery query = DataQuery.of("EnderChestInventory");

        if (dataContainer.isEmpty() || !dataContainer.contains(query)) {
            dataContainer.set(query, Collections.emptyMap());
        }

        FinderFile.writeToFile(dataContainer, path);
    }

    public static void serializePlayerData(Path path, Path inputPath) {
        DataQuery inventoryQuery = DataQuery.of("EnderItems");
        DataContainer dataContainer = DataContainer.createNew();

        DataQuery contentVersionQuery = DataQuery.of("ContentVersion");
        DataQuery slotQuery = DataQuery.of("Slot");
        DataQuery itemTypeQuery = DataQuery.of("ItemType");
        DataQuery idQuery = DataQuery.of("id");
        DataQuery countQuery = DataQuery.of("Count");
        DataQuery damageQuery = DataQuery.of("Damage");
        DataQuery unsafeDamageQuery = DataQuery.of("UnsafeDamage");

        FinderFile.readFromNbtFile(inputPath)
                .flatMap(playerDataContainer -> playerDataContainer.getViewList(inventoryQuery))
                .ifPresent(viewList -> viewList.forEach(view -> {
                    if (!view.contains(slotQuery, idQuery, countQuery, damageQuery)) {
                        return;
                    }

                    Optional<Integer> optionalSlotIndex = view.getByte(slotQuery).map(Byte::intValue);

                    if (!optionalSlotIndex.isPresent()) {
                        return;
                    }

                    int slotIndex = optionalSlotIndex.get();
                    DataQuery slotPath = DataQuery.of("EnderChestInventory", Integer.toString(slotIndex));
                    DataContainer itemStackContainer = DataContainer.createNew().set(contentVersionQuery, 1);

                    view.getString(idQuery)
                            .ifPresent(value -> itemStackContainer.set(itemTypeQuery, value));

                    view.getByte(countQuery)
                            .map(Byte::intValue)
                            .ifPresent(value -> itemStackContainer.set(countQuery, value));

                    view.getByte(damageQuery)
                            .map(Byte::intValue)
                            .ifPresent(value -> itemStackContainer.set(unsafeDamageQuery, value));

                    if (!itemStackContainer.contains(itemTypeQuery, countQuery, unsafeDamageQuery)) {
                        return;
                    }

                    dataContainer.set(slotPath, itemStackContainer);
                }));

        serialize(path, dataContainer);
    }

    public static void deserialize(Path path, Player player) {
        Optional<DataContainer> optionalDataContainer = FinderFile.readFromNbtFile(path);

        if (!optionalDataContainer.isPresent()) {
            player.getEnderChestInventory().clear();
            return;
        }

        deserialize(optionalDataContainer.get(), player);
    }

    public static void deserialize(DataContainer dataContainer, Player player) {
        int index = 0;
        for (Inventory slot : player.getEnderChestInventory().slots()) {
            dataContainer.getView(DataQuery.of("EnderChestInventory", Integer.toString(index)))
                    .flatMap(itemView -> Sponge.getDataManager().deserialize(ItemStack.class, itemView))
                    .ifPresent(slot::set);
            index++;
        }
    }

}
