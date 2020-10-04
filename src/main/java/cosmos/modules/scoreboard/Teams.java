package cosmos.modules.scoreboard;

import cosmos.commands.scoreboard.teams.Add;
import cosmos.commands.scoreboard.teams.Empty;
import cosmos.commands.scoreboard.teams.Join;
import cosmos.commands.scoreboard.teams.Leave;
import cosmos.commands.scoreboard.teams.List;
import cosmos.commands.scoreboard.teams.Option;
import cosmos.commands.scoreboard.teams.Remove;
import cosmos.modules.AbstractModule;

public class Teams extends AbstractModule {

    public Teams() {
        super("Teams module commands");

        addChild(new Add());
        addChild(new Empty());
        addChild(new Join());
        addChild(new Leave());
        addChild(new List());
        addChild(new Option());
        addChild(new Remove());
    }
}
