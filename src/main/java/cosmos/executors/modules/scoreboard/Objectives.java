package cosmos.executors.modules.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.objectives.Add;
import cosmos.executors.commands.scoreboard.objectives.List;
import cosmos.executors.commands.scoreboard.objectives.Remove;
import cosmos.executors.commands.scoreboard.objectives.SetDisplay;
import cosmos.executors.modules.AbstractModule;
import cosmos.executors.modules.scoreboard.objectives.Modify;

@Singleton
public class Objectives extends AbstractModule {

    @Inject
    Objectives(final Injector injector) {
        super(
                injector.getInstance(Add.class),
                injector.getInstance(List.class),
                injector.getInstance(Modify.class),
                injector.getInstance(Remove.class),
                injector.getInstance(SetDisplay.class)
        );
    }

}
