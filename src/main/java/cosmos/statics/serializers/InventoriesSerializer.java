package cosmos.statics.serializers;

import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;

import java.nio.file.Path;
import java.util.Optional;

public class InventoriesSerializer {

    public static void serialize(Path path, Player player) {
        DataContainer dataContainer = DataContainer.createNew();

        int index = 0;
        for (Inventory slot : player.getInventory().slots()) {
            DataQuery slotPath = DataQuery.of("Inventory", Integer.toString(index));
            slot.peek().ifPresent(itemStack -> dataContainer.set(slotPath, itemStack));
            index++;
        }

        FinderFile.writeToFile(dataContainer, path);
    }

    public static void deserialize(Path path, Player player) {
        Optional<DataContainer> optionalDataContainer = FinderFile.readFromFile(path);

        if (!optionalDataContainer.isPresent()) {
            player.getInventory().clear();
            return;
        }

        int index = 0;
        for (Inventory slot : player.getInventory().slots()) {
            optionalDataContainer.get().getView(DataQuery.of("Inventory", Integer.toString(index)))
                    .flatMap(itemView -> Sponge.getDataManager().deserialize(ItemStack.class, itemView))
                    .ifPresent(slot::set);
            index++;
        }
    }

}
