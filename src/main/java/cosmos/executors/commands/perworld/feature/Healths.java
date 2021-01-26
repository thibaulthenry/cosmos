package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.GameModesListener;
import cosmos.registries.listener.impl.perworld.HealthsListener;

@Singleton
public class Healths extends AbstractPerWorldFeatureCommand {

    public Healths() {
        super(HealthsListener.class);
    }

}
