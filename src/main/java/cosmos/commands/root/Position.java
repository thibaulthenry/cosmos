package cosmos.commands.root;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.arguments.SelectorArguments;
import cosmos.statics.handlers.OutputFormatter;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.Tamer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

public class Position extends AbstractCommand {

    public Position() {
        super(
                Arguments.limitCompleteElement(
                        SelectorArguments.entityOrSource(ArgKeys.TARGETS.t)
                ),
                GenericArguments.optional(
                        SelectorArguments.playerOrSource(ArgKeys.MESSAGE_RECEIVER.t)
                )
        );
    }

    @SuppressWarnings("HardcodedFileSeparator")
    private static Text getPositionText(Entity target, MessageReceiver messageReceiver) {
        Location<World> transmitterLocation = target.getLocation();
        String transmitterName = target instanceof Tamer ? ((Tamer) target).getName() : target.getUniqueId().toString();

        Text.Builder textBuilder = Text.of(OutputFormatter.formatOutput(transmitterLocation.getPosition())).toBuilder();

        boolean self = Move.isSelf(messageReceiver, target);

        if (!self) {
            textBuilder.onHover(TextActions.showText(Text.of("Click to join " + transmitterName)));
            textBuilder.onClick(TextActions.suggestCommand("/cm moveto " + transmitterName + " " + ((Tamer) messageReceiver).getName()));
        }

        return Outputs.SHOW_POSITION.asText(
                self ? "You" : transmitterName, self ? "are" : "is",
                textBuilder, transmitterLocation.getExtent().getName()
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        CommandSource commandSource = args.<CommandSource>getOne(ArgKeys.MESSAGE_RECEIVER.t)
                .orElse(src);

        Collection<Text> contents = args.<Entity>getAll(ArgKeys.TARGETS.t)
                .stream()
                .filter(entity -> !Objects.isNull(entity))
                .map(target -> getPositionText(target, commandSource))
                .collect(Collectors.toList());

        sendPaginatedOutput(commandSource, Outputs.SHOW_POSITIONS.asText(contents.size()), contents, true);
    }
}
