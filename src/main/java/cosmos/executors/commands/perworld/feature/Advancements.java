package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.AdvancementsListener;

@Singleton
public class Advancements extends AbstractPerWorldFeatureCommand {

    public Advancements() {
        super(AdvancementsListener.class);
    }

}
