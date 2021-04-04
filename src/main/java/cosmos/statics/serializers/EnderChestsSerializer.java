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

        if (dataContainer.isEmpty()) {
            dataContainer.set(DataQuery.of("EnderChestInventory"), Collections.emptyMap());
        }

        FinderFile.writeToFile(dataContainer, path);
    }

    public static void deserialize(Path path, Player player) {
        Optional<DataContainer> optionalDataContainer = FinderFile.readFromFile(path);

        if (!optionalDataContainer.isPresent()) {
            player.getEnderChestInventory().clear();
            return;
        }

        int index = 0;
        for (Inventory slot : player.getEnderChestInventory().slots()) {
            optionalDataContainer.get().getView(DataQuery.of("EnderChestInventory", Integer.toString(index)))
                    .flatMap(itemView -> Sponge.getDataManager().deserialize(ItemStack.class, itemView))
                    .ifPresent(slot::set);
            index++;
        }
    }

}
