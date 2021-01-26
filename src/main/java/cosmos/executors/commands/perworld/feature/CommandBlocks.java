package cosmos.executors.commands.perworld.feature;

import com.google.inject.Singleton;
import cosmos.registries.listener.impl.perworld.ChatsListener;
import cosmos.registries.listener.impl.perworld.CommandBlocksListener;

@Singleton
public class CommandBlocks extends AbstractPerWorldFeatureCommand {

    public CommandBlocks() {
        super(CommandBlocksListener.class);
    }

}
