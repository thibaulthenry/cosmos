package cosmos.executors.modules.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.teams.Add;
import cosmos.executors.commands.scoreboard.teams.Empty;
import cosmos.executors.commands.scoreboard.teams.Join;
import cosmos.executors.commands.scoreboard.teams.Leave;
import cosmos.executors.commands.scoreboard.teams.List;
import cosmos.executors.commands.scoreboard.teams.Modify;
import cosmos.executors.commands.scoreboard.teams.Remove;
import cosmos.executors.modules.AbstractModule;

@Singleton
public class Teams extends AbstractModule {

    @Inject
    public Teams(final Injector injector) {
        super(
                injector.getInstance(Add.class),
                injector.getInstance(Empty.class),
                injector.getInstance(Join.class),
                injector.getInstance(Leave.class),
                injector.getInstance(List.class),
                injector.getInstance(Modify.class),
                injector.getInstance(Remove.class)
        );
    }
}
