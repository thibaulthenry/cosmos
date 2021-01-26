package cosmos.executors.modules.scoreboard.teams;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.teams.modify.CollisionRule;
import cosmos.executors.commands.scoreboard.teams.modify.Color;
import cosmos.executors.commands.scoreboard.teams.modify.DeathMessageVisibility;
import cosmos.executors.commands.scoreboard.teams.modify.DisplayName;
import cosmos.executors.commands.scoreboard.teams.modify.FriendlyFire;
import cosmos.executors.commands.scoreboard.teams.modify.NameTagVisibility;
import cosmos.executors.commands.scoreboard.teams.modify.Prefix;
import cosmos.executors.commands.scoreboard.teams.modify.SeeFriendlyInvisibles;
import cosmos.executors.commands.scoreboard.teams.modify.Suffix;
import cosmos.executors.modules.AbstractModule;
import cosmos.executors.parameters.impl.scoreboard.TeamAll;

@Singleton
public class Modify extends AbstractModule {

    @Inject
    public Modify(final Injector injector) {
        super(
                injector.getInstance(TeamAll.class).build(),
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
