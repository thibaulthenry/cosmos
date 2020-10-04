package cosmos.constants;

import cosmos.listeners.AbstractListener;
import cosmos.listeners.perworld.AdvancementsListener;
import cosmos.listeners.perworld.ChatsListener;
import cosmos.listeners.perworld.CommandBlocksListener;
import cosmos.listeners.perworld.ExperiencesListener;
import cosmos.listeners.perworld.GameModesListener;
import cosmos.listeners.perworld.HealthsListener;
import cosmos.listeners.perworld.HungersListener;
import cosmos.listeners.perworld.InventoriesListener;
import cosmos.listeners.perworld.ScoreboardsListener;
import cosmos.listeners.perworld.TabListsListener;

public enum PerWorldCommands {

    ADVANCEMENTS(AdvancementsListener.class),
    CHATS(ChatsListener.class),
    COMMAND_BLOCKS(CommandBlocksListener.class),
    EXPERIENCES(ExperiencesListener.class),
    GAME_MODES(GameModesListener.class),
    HEALTHS(HealthsListener.class),
    HUNGERS(HungersListener.class),
    INVENTORIES(InventoriesListener.class),
    SCOREBOARDS(ScoreboardsListener.class),
    TAB_LISTS(TabListsListener.class);

    private final Class<? extends AbstractListener> listenerClass;

    PerWorldCommands(Class<? extends AbstractListener> listenerClass) {
        this.listenerClass = listenerClass;
    }

    public Class<? extends AbstractListener> getListenerClass() {
        return listenerClass;
    }

    public String getName() {
        return name().toLowerCase().replace("_", "");
    }
}
