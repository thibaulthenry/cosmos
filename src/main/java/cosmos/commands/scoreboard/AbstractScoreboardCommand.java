package cosmos.commands.scoreboard;

import cosmos.commands.properties.AbstractPropertyCommand;
import cosmos.constants.Outputs;
import cosmos.listeners.ListenerRegister;
import cosmos.listeners.perworld.ScoreboardsListener;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.scoreboard.Scoreboard;
import org.spongepowered.api.world.storage.WorldProperties;

public abstract class AbstractScoreboardCommand extends AbstractPropertyCommand {

    private Scoreboard scoreboard;

    protected AbstractScoreboardCommand(CommandElement... commandElements) {
        super(commandElements);
    }

    protected Scoreboard getScoreboard() {
        return scoreboard;
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        if (!ListenerRegister.isListenerRegistered(ScoreboardsListener.class)) {
            throw Outputs.PER_WORLD_SCOREBOARDS_DISABLED.asException();
        }

        scoreboard = ScoreboardsListener.getScoreboard(worldProperties.getUniqueId());
        runWithScoreboard(src, args, worldProperties.getWorldName(), scoreboard);
    }

    protected abstract void runWithScoreboard(CommandSource src, CommandContext args, String worldName, Scoreboard scoreboard) throws CommandException;

}
