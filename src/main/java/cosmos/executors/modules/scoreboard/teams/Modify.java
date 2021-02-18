package cosmos.executors.modules.scoreboard.teams;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.scoreboard.teams.modify.*;
import cosmos.executors.modules.AbstractModule;

@Singleton
public class Modify extends AbstractModule {

    @Inject
    Modify(final Injector injector) {
        super(
                CosmosParameters.TEAM_ALL.get().build(),
                false,
                injector.getInstance(CollisionRule.class),
                injector.getInstance(Color.class),
                injector.getInstance(DeathMessageVisibility.class),
                injector.getInstance(DisplayName.class),
                injector.getInstance(FriendlyFire.class),
                injector.getInstance(NameTagVisibility.class),
                injector.getInstance(Prefix.class),
                injector.getInstance(SeeFriendlyInvisibles.class),
                injector.getInstance(Suffix.class)
        );
    }

}
