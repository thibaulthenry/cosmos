package cosmos.modules.scoreboard;

import cosmos.commands.scoreboard.players.Add;
import cosmos.commands.scoreboard.players.Get;
import cosmos.commands.scoreboard.players.List;
import cosmos.commands.scoreboard.players.Operation;
import cosmos.commands.scoreboard.players.Random;
import cosmos.commands.scoreboard.players.Remove;
import cosmos.commands.scoreboard.players.Reset;
import cosmos.commands.scoreboard.players.Set;
import cosmos.commands.scoreboard.players.Test;
import cosmos.modules.AbstractModule;

public class Players extends AbstractModule {

    public Players() {
        super("Players module commands", true);

        addChild(new Add());
        //addChild(new Enable());
        addChild(new Get());
        addChild(new List());
        addChild(new Operation());
        addChild(new Random());
        addChild(new Remove());
        addChild(new Reset());
        addChild(new Set());
        //addChild(new Tag());
        addChild(new Test());
    }
}
