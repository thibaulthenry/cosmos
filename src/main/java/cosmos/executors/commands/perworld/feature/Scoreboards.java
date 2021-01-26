package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.ChatsListener;
import cosmos.registries.listener.impl.perworld.ScoreboardsListener;

@Singleton
public class Scoreboards extends AbstractPerWorldFeatureCommand {

    public Scoreboards() {
        super(ScoreboardsListener.class);
    }

}
