package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Helps;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.text.Text;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

public class Help extends AbstractCommand {

    public Help() {
        super(
                GenericArguments.optional(
                        Arguments.limitCompleteElement(
                                Arguments.helpChoices(ArgKeys.COMMAND)
                        )
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        Optional<Helps> optionalHelp = args.getOne(ArgKeys.COMMAND.t);

        if (optionalHelp.isPresent()) {
            src.sendMessage(optionalHelp.get().getText());
            return;
        }

        Collection<Text> contents = Arrays
                .stream(Helps.values())
                .map(Helps::getFlatText)
                .collect(Collectors.toList());

        Text title = Outputs.SHOW_HELP.asText(contents.size());

        sendPaginatedOutput(src, title, contents, false);
    }
}
