package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.HungersListener;
import cosmos.registries.listener.impl.perworld.InventoriesListener;

@Singleton
public class Inventories extends AbstractPerWorldFeatureCommand {

    public Inventories() {
        super(InventoriesListener.class);
    }

}
