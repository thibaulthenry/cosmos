package cosmos.executors.modules.scoreboard.objectives;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.objectives.modify.DisplayName;
import cosmos.executors.commands.scoreboard.objectives.modify.RenderType;
import cosmos.executors.modules.AbstractModule;
import cosmos.executors.parameters.impl.scoreboard.ObjectiveAll;

@Singleton
public class Modify extends AbstractModule {

    @Inject
    public Modify(final Injector injector) {
        super(
                injector.getInstance(ObjectiveAll.class).build(),
                injector.getInstance(DisplayName.class),
                injector.getInstance(RenderType.class)
        );
    }

}
