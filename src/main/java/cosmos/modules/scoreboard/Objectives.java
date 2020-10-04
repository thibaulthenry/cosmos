package cosmos.modules.scoreboard;

import cosmos.commands.scoreboard.objectives.Add;
import cosmos.commands.scoreboard.objectives.List;
import cosmos.commands.scoreboard.objectives.Modify;
import cosmos.commands.scoreboard.objectives.Remove;
import cosmos.commands.scoreboard.objectives.SetDisplay;
import cosmos.modules.AbstractModule;

public class Objectives extends AbstractModule {

    public Objectives() {
        super("Objectives module commands");

        addChild(new Add());
        addChild(new List());
        addChild(new Remove());
        addChild(new Modify());
        addChild(new SetDisplay());
    }
}
