package cosmos.commands.perworld;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.ListenerRegister;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class Information extends AbstractCommand {

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        PerWorldCommands perWorldCommands = args.<PerWorldCommands>getOne(ArgKeys.PER_WORLD_COMMAND.t)
                .orElseThrow(Outputs.INVALID_PER_WORLD_COMMAND_CHOICE.asSupplier());

        boolean state = ListenerRegister.isListenerRegistered(perWorldCommands.getListenerClass());

        Set<Text> bypassing = Bypass.getAllBypass(perWorldCommands)
                .stream()
                .map(playerId -> Text.builder()
                        .append(Text.of(playerId.getSecond()))
                        .onHover(TextActions.showText(Text.of(playerId.getFirst())))
                        .build()
                )
                .collect(Collectors.toSet());

        Collection<Text> groups = GroupRegister.collectGroups(perWorldCommands)
                .stream()
                .map(group -> group.stream().map(worldName -> Text.of(TextColors.DARK_GREEN, worldName)).collect(Collectors.toList()))
                .map(group -> Text.joinWith(Text.of(TextColors.GRAY, ", "), group))
                .map(text -> Text.of(TextColors.GRAY, "[", text, "]"))
                .collect(Collectors.toList());

        Text perWorldInformation = Text.of(
                TextColors.BLUE, "Showing per-world information about ", TextColors.WHITE, perWorldCommands.getName(),
                node("State", state ? "enabled" : "disabled"),
                node("Groups", Text.joinWith(Text.of(TextColors.DARK_GRAY, ", "), groups)),
                node("Players bypassing", bypassing.isEmpty() ? "none" : Text.joinWith(Text.of(TextColors.GRAY, ", "), bypassing))
        );

        src.sendMessage(perWorldInformation);
    }

    private static Text node(String label, Object value, boolean breakLine) {
        return Text.of(
                Text.NEW_LINE, breakLine ? Text.NEW_LINE : Text.EMPTY, TextColors.WHITE, " • ",
                TextColors.GRAY, label, ": ", TextColors.GREEN, value
        );
    }

    private static Text node(String label, Object value) {
        return node(label, value, false);
    }

}
