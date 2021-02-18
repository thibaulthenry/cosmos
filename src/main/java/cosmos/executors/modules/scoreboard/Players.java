package cosmos.executors.modules.scoreboard;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.players.Add;
import cosmos.executors.commands.scoreboard.players.Enable;
import cosmos.executors.commands.scoreboard.players.Get;
import cosmos.executors.commands.scoreboard.players.List;
import cosmos.executors.commands.scoreboard.players.Operation;
import cosmos.executors.commands.scoreboard.players.Random;
import cosmos.executors.commands.scoreboard.players.Remove;
import cosmos.executors.commands.scoreboard.players.Reset;
import cosmos.executors.commands.scoreboard.players.Set;
import cosmos.executors.commands.scoreboard.players.Test;
import cosmos.executors.modules.AbstractModule;

@Singleton
public class Players extends AbstractModule {

    @Inject
    Players(final Injector injector) {
        super(
                injector.getInstance(Add.class),
                injector.getInstance(Enable.class),
                injector.getInstance(Get.class),
                injector.getInstance(List.class),
                injector.getInstance(Operation.class),
                injector.getInstance(Random.class),
                injector.getInstance(Remove.class),
                injector.getInstance(Reset.class),
                injector.getInstance(Set.class),
                injector.getInstance(Test.class)
        );
    }

}
