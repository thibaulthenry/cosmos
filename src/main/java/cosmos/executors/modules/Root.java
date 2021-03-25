package cosmos.executors.modules;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.executors.commands.root.Back;
import cosmos.executors.commands.root.Copy;
import cosmos.executors.commands.root.Delete;
import cosmos.executors.commands.root.Help;
import cosmos.executors.commands.root.Import;
import cosmos.executors.commands.root.Information;
import cosmos.executors.commands.root.List;
import cosmos.executors.commands.root.Load;
import cosmos.executors.commands.root.Move;
import cosmos.executors.commands.root.MoveTo;
import cosmos.executors.commands.root.New;
import cosmos.executors.commands.root.Position;
import cosmos.executors.commands.root.Rename;
import cosmos.executors.commands.root.Unload;

@Singleton
public class Root extends AbstractModule {

    @Inject
    Root(final Injector injector) {
        super(
                injector.getInstance(Back.class),
                injector.getInstance(Backup.class),
                injector.getInstance(Border.class),
                injector.getInstance(Copy.class),
                injector.getInstance(Delete.class),
                injector.getInstance(Help.class),
                injector.getInstance(Import.class),
                injector.getInstance(Information.class),
                injector.getInstance(List.class),
                injector.getInstance(Load.class),
                //injector.getInstance(Move.class),
                injector.getInstance(MoveTo.class),
                injector.getInstance(New.class),
                injector.getInstance(PerWorld.class),
                injector.getInstance(Portal.class),
                injector.getInstance(Position.class),
                injector.getInstance(Properties.class),
                injector.getInstance(Rename.class),
                injector.getInstance(Scoreboard.class),
                injector.getInstance(Time.class),
                injector.getInstance(Unload.class),
                injector.getInstance(Weather.class)
        );
    }

}
