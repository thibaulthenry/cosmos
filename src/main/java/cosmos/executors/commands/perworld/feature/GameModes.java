package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.GameModesListener;

@Singleton
public class GameModes extends AbstractPerWorldFeatureCommand {

    public GameModes() {
        super(GameModesListener.class);
    }

}
