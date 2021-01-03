package cosmos.executors.commands.scoreboard.teams;

import com.google.inject.Singleton;
import cosmos.executors.commands.scoreboard.AbstractScoreboardCommand;
import net.kyori.adventure.audience.Audience;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.exception.CommandException;
import org.spongepowered.api.command.parameter.CommandContext;
import org.spongepowered.api.scoreboard.Scoreboard;

@Singleton
public class Modify extends AbstractScoreboardCommand {
    // todo like gamerules

    @Override
    protected void run(Audience src, CommandContext context, ResourceKey worldKey, Scoreboard scoreboard) throws CommandException {

    }
}
