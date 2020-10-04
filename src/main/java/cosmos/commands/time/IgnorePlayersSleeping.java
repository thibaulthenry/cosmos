package cosmos.commands.time;

import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.listeners.time.IgnorePlayersSleepingListener;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.config.Config;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class IgnorePlayersSleeping extends AbstractTimeCommand {

    private static final Collection<UUID> computedWorlds = new ArrayList<>();

    public IgnorePlayersSleeping() {
        super(
                GenericArguments.optionalWeak(
                        GenericArguments.bool(ArgKeys.STATE.t)
                ),
                Arguments.baseFlag(ArgKeys.SAVE_CONFIG)
        );
    }

    public static void enableSleepIgnoranceFromConfig() {
        if (!Config.isListenerEnabled(IgnorePlayersSleepingListener.class)) {
            computedWorlds.clear();
            return;
        }

        FinderWorldProperties.getAllWorldProperties(true)
                .stream()
                .filter(worldProperties -> !doesIgnorePlayersSleeping(worldProperties))
                .filter(Config::isIgnorePlayersSleepingWorldEnabled)
                .forEach(IgnorePlayersSleeping::enableSleepIgnorance);
    }

    public static void enableSleepIgnorance(WorldProperties worldProperties) {
        computedWorlds.add(worldProperties.getUniqueId());
        setPlayersSleepingIgnored(worldProperties, true);
    }

    public static boolean doesIgnorePlayersSleeping(WorldProperties worldProperties) {
        return computedWorlds.contains(worldProperties.getUniqueId());
    }

    private static void disableSleepIgnorance(WorldProperties worldProperties) {
        computedWorlds.remove(worldProperties.getUniqueId());
        setPlayersSleepingIgnored(worldProperties, false);
    }

    private static void setPlayersSleepingIgnored(WorldProperties worldProperties, boolean state) {
        Sponge.getServer().getWorld(worldProperties.getUniqueId())
                .map(World::getPlayers)
                .ifPresent(players -> players.forEach(player -> player.setSleepingIgnored(state)));
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        if (!Config.isListenerEnabled(IgnorePlayersSleepingListener.class)) {
            throw Outputs.SLEEP_IGNORANCE_GLOBALLY_DISABLED.asException();
        }

        Optional<Boolean> optionalState = args.getOne(ArgKeys.STATE.t);
        boolean state = doesIgnorePlayersSleeping(worldProperties);
        String mutableText = "currently";

        if (optionalState.isPresent()) {
            state = optionalState.get();
            mutableText = "successfully";
            if (state) {
                if (computedWorlds.contains(worldProperties.getUniqueId())) {
                    throw Outputs.SLEEP_IGNORANCE_ALREADY_ACTIVATED.asException(worldProperties);
                }

                enableSleepIgnorance(worldProperties);
            } else {
                if (!computedWorlds.contains(worldProperties.getUniqueId())) {
                    throw Outputs.SLEEP_IGNORANCE_ALREADY_DISABLED.asException(worldProperties);
                }

                disableSleepIgnorance(worldProperties);
            }
        }

        src.sendMessage(
                Outputs.IGNORE_PLAYERS_SLEEPING.asText(
                        worldProperties,
                        mutableText,
                        state ? "activated" : "disabled"
                )
        );

        if (!args.hasAny(ArgKeys.SAVE_CONFIG.t)) {
            return;
        }

        if (!Config.saveIgnorePlayerSleeping(worldProperties, state)) {
            src.sendMessage(Outputs.SAVING_CONFIG_NODE.asText("Sleep ignorance"));
            return;
        }

        src.sendMessage(Outputs.SAVE_CONFIG_NODE.asText("Sleep ignorance"));
    }
}
