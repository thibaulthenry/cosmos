package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.AdvancementsListener;
import cosmos.registries.listener.impl.perworld.ChatsListener;

@Singleton
public class Chats extends AbstractPerWorldFeatureCommand {

    public Chats() {
        super(ChatsListener.class);
    }

}
