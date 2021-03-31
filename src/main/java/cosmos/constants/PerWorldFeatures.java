package cosmos.constants;

import com.google.common.base.CaseFormat;
import cosmos.registries.listener.impl.perworld.AbstractPerWorldListener;
import cosmos.registries.listener.impl.perworld.AdvancementListener;
import cosmos.registries.listener.impl.perworld.ChatListener;
import cosmos.registries.listener.impl.perworld.CommandBlockListener;
import cosmos.registries.listener.impl.perworld.ExperienceListener;
import cosmos.registries.listener.impl.perworld.GameModeListener;
import cosmos.registries.listener.impl.perworld.HealthListener;
import cosmos.registries.listener.impl.perworld.HungerListener;
import cosmos.registries.listener.impl.perworld.InventoryListener;
import cosmos.registries.listener.impl.perworld.ScoreboardListener;
import cosmos.registries.listener.impl.perworld.TabListListener;

public enum PerWorldFeatures {

    ADVANCEMENT(AdvancementListener.class),
    CHAT(ChatListener.class),
    COMMAND_BLOCK(CommandBlockListener.class),
    EXPERIENCE(ExperienceListener.class),
    GAME_MODE(GameModeListener.class),
    HEALTH(HealthListener.class),
    HUNGER(HungerListener.class),
    INVENTORY(InventoryListener.class),
    SCOREBOARD(ScoreboardListener.class),
    TAB_LIST(TabListListener.class);

    private final Class<? extends AbstractPerWorldListener> listenerClass;

    PerWorldFeatures(final Class<? extends AbstractPerWorldListener> listenerClass) {
        this.listenerClass = listenerClass;
    }

    public Class<? extends AbstractPerWorldListener> listenerClass() {
        return this.listenerClass;
    }

    public String formatted() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, this.name());
    }

}
