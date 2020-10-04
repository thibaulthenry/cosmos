package cosmos.commands.scoreboard.players.tag;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class Add extends AbstractTagCommand {

    public Add() {
        super(GenericArguments.string(ArgKeys.VALUE.t));
    }

    @Override
    protected void runWithEntities(CommandSource src, CommandContext args, String worldName, Map<Text, Entity> entities) throws CommandException {
        Collection<Text> contents = entities.entrySet()
                .stream()
                .map(entry -> Text.EMPTY)
                .collect(Collectors.toList());

        Text title = Outputs.ADD_ENTITIES_TAG.asText(contents.size(), worldName);

        sendPaginatedOutput(src, title, contents, true);
    }
}
