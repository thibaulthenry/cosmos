package cosmos.commands.backup;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.WorldNameArguments;
import cosmos.statics.finders.FinderBackup;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

import java.util.Collection;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class List extends AbstractCommand {

    public List() {
        super(
                GenericArguments.optional(
                        Arguments.limitCompleteElement(
                                WorldNameArguments.backupWorldChoices(ArgKeys.WORLD)
                        )
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> optionalWorldName = args.getOne(ArgKeys.WORLD.t);

        Locale locale = src.getLocale();

        Collection<Text> contents = FinderBackup.getBackups()
                .stream()
                .filter(backupArchetype -> !optionalWorldName.isPresent() ||
                        optionalWorldName.get().equals(backupArchetype.getWorldName()))
                .map(backupArchetype -> backupArchetype.toText(locale))
                .collect(Collectors.toList());

        Text title = optionalWorldName
                .map(worldName -> Outputs.SHOW_WORLD_BACKUPS.asText(contents.size(), worldName))
                .orElseGet(() -> Outputs.SHOW_ALL_BACKUPS.asText(contents.size()));

        sendPaginatedOutput(src, title, contents, false);
    }

}
