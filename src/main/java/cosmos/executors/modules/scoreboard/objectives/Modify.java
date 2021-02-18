package cosmos.executors.modules.scoreboard.objectives;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.CosmosParameters;
import cosmos.executors.commands.scoreboard.objectives.modify.DisplayName;
import cosmos.executors.commands.scoreboard.objectives.modify.RenderType;
import cosmos.executors.modules.AbstractModule;

@Singleton
public class Modify extends AbstractModule {

    @Inject
    Modify(final Injector injector) {
        super(
                CosmosParameters.OBJECTIVE_ALL.get().build(),
                false,
                injector.getInstance(DisplayName.class),
                injector.getInstance(RenderType.class)
        );
    }

}
