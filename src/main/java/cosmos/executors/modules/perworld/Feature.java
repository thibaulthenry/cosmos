package cosmos.executors.modules.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import cosmos.executors.commands.perworld.feature.Advancements;
import cosmos.executors.commands.perworld.feature.Chats;
import cosmos.executors.commands.perworld.feature.CommandBlocks;
import cosmos.executors.commands.perworld.feature.Experiences;
import cosmos.executors.commands.perworld.feature.GameModes;
import cosmos.executors.commands.perworld.feature.Healths;
import cosmos.executors.commands.perworld.feature.Hungers;
import cosmos.executors.commands.perworld.feature.Inventories;
import cosmos.executors.commands.perworld.feature.Scoreboards;
import cosmos.executors.commands.perworld.feature.TabLists;
import cosmos.executors.modules.AbstractModule;

public class Feature extends AbstractModule {

    @Inject
    Feature(final Injector injector) {
        super(
                injector.getInstance(Advancements.class),
                injector.getInstance(Chats.class),
                injector.getInstance(CommandBlocks.class),
                injector.getInstance(Experiences.class),
                injector.getInstance(GameModes.class),
                injector.getInstance(Healths.class),
                injector.getInstance(Hungers.class),
                injector.getInstance(Inventories.class),
                injector.getInstance(Scoreboards.class),
                injector.getInstance(TabLists.class)
        );
    }
}
