package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.GameModesListener;
import cosmos.registries.listener.impl.perworld.HungersListener;

@Singleton
public class Hungers extends AbstractPerWorldFeatureCommand {

    public Hungers() {
        super(HungersListener.class);
    }

}
