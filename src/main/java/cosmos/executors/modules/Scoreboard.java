package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.modules.scoreboard.Objectives;
import cosmos.executors.modules.scoreboard.Players;
import cosmos.executors.modules.scoreboard.Teams;

@Singleton
class Scoreboard extends AbstractModule {

    @Inject
    Scoreboard(final Injector injector) {
        super(
                CosmosParameters.WORLD_ALL.get().optional().build(),
                injector.getInstance(Objectives.class),
                injector.getInstance(Players.class),
                injector.getInstance(Teams.class)
        );
    }

}
