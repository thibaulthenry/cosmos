package cosmos.modules;

import cosmos.commands.root.Delete;
import cosmos.commands.root.Disable;
import cosmos.commands.root.Duplicate;
import cosmos.commands.root.Enable;
import cosmos.commands.root.Help;
import cosmos.commands.root.Import;
import cosmos.commands.root.Information;
import cosmos.commands.root.List;
import cosmos.commands.root.Load;
import cosmos.commands.root.Move;
import cosmos.commands.root.MoveTo;
import cosmos.commands.root.New;
import cosmos.commands.root.Position;
import cosmos.commands.root.Rename;
import cosmos.commands.root.Unload;
import cosmos.commands.root.ViewDistance;

public class Root extends AbstractModule {

    public Root() {
        super("Cosmos commands | Worlds management plugin", true);

        addChild(new Backup());
        addChild(new Border());
        addChild(new Delete());
        addChild(new Disable());
        addChild(new Duplicate());
        addChild(new Enable());
        addChild(new Help());
        addChild(new Import());
        addChild(new Information());
        addChild(new List());
        addChild(new Load());
        addChild(new Move());
        addChild(new MoveTo());
        addChild(new New());
        addChild(new PerWorld());
        addChild(new Position());
        addChild(new Properties());
        addChild(new Rename());
        addChild(new Scoreboard());
        addChild(new Time());
        addChild(new Unload());
        addChild(new ViewDistance());
        addChild(new Weather());
    }
}
