package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.modules.scoreboard.Objectives;
import cosmos.executors.modules.scoreboard.Players;
import cosmos.executors.modules.scoreboard.Teams;
import cosmos.executors.parameters.impl.world.WorldAll;

@Singleton
class Scoreboard extends AbstractModule {

    @Inject
    Scoreboard(final Injector injector) {
        super(
                injector.getInstance(WorldAll.class).optional().build(),
                injector.getInstance(Objectives.class),
                injector.getInstance(Players.class),
                injector.getInstance(Teams.class)
        );
    }

}
