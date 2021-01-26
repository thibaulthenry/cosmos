package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.ExperiencesListener;
import cosmos.registries.listener.impl.perworld.TabListsListener;

@Singleton
public class TabLists extends AbstractPerWorldFeatureCommand {

    public TabLists() {
        super(TabListsListener.class);
    }

}
