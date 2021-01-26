package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.CommandBlocksListener;
import cosmos.registries.listener.impl.perworld.ExperiencesListener;

@Singleton
public class Experiences extends AbstractPerWorldFeatureCommand {

    public Experiences() {
        super(ExperiencesListener.class);
    }

}
