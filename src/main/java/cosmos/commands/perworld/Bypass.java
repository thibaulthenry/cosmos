package cosmos.commands.perworld;

import cosmos.commands.AbstractCommand;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.PerWorldCommands;
import cosmos.listeners.ListenerRegister;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.util.Tuple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class Bypass extends AbstractCommand {

    private static final Map<PerWorldCommands, Set<Tuple<UUID, String>>> computedPlayers = new HashMap<>();

    public Bypass() {
        super(
                GenericArguments.playerOrSource(ArgKeys.PLAYER.t),
                GenericArguments.optional(
                        GenericArguments.bool(ArgKeys.STATE.t)
                )
        );
    }

    @Override
    protected void run(CommandSource src, CommandContext args) throws CommandException {
        PerWorldCommands perWorldCommands = args.<PerWorldCommands>getOne(ArgKeys.PER_WORLD_COMMAND.t)
                .orElseThrow(Outputs.INVALID_PER_WORLD_COMMAND_CHOICE.asSupplier());

        if (perWorldCommands.equals(PerWorldCommands.COMMAND_BLOCKS)) {
            throw Outputs.BYPASS_PER_WORLD_COMMAND_BLOCKS.asException(PerWorldCommands.COMMAND_BLOCKS.getName());
        }

        if (!ListenerRegister.isListenerRegistered(perWorldCommands.getListenerClass())) {
            throw Outputs.PER_WORLD_DISABLED.asException(perWorldCommands.getName());
        }

        Player player = args.<Player>getOne(ArgKeys.PLAYER.t)
                .orElseThrow(Outputs.INVALID_VALUE.asSupplier());

        Tuple<UUID, String> playerId = Tuple.of(player.getUniqueId(), player.getName());

        if (args.<Boolean>getOne(ArgKeys.STATE.t).orElse(true)) {
            if (doesBypass(perWorldCommands, player)) {
                src.sendMessage(Outputs.BYPASSING_PER_WORLD_LISTENER_START.asText(player.getName(), perWorldCommands.getName()));
            } else {
                computedPlayers.computeIfAbsent(perWorldCommands, key -> new HashSet<>()).add(playerId);
                ListenerRegister.triggerToggleListener(perWorldCommands.getListenerClass(), false, player);
                src.sendMessage(Outputs.BYPASS_PER_WORLD_LISTENER_START.asText(player.getName(), perWorldCommands.getName()));
            }
        } else {
            if (doesBypass(perWorldCommands, player)) {
                computedPlayers.get(perWorldCommands).remove(playerId);
                ListenerRegister.triggerToggleListener(perWorldCommands.getListenerClass(), true, player);
                src.sendMessage(Outputs.BYPASS_PER_WORLD_LISTENER_STOP.asText(player.getName(), perWorldCommands.getName()));
            } else {
                src.sendMessage(Outputs.BYPASSING_PER_WORLD_LISTENER_STOP.asText(player.getName(), perWorldCommands.getName()));
            }
        }

    }

    public static boolean doesBypass(PerWorldCommands feature, Player player) {
        return computedPlayers.containsKey(feature) && computedPlayers.get(feature).contains(Tuple.of(player.getUniqueId(), player.getName()));
    }

    public static Set<Tuple<UUID, String>> getAllBypass(PerWorldCommands feature) {
        return computedPlayers.getOrDefault(feature, new HashSet<>());
    }

}
