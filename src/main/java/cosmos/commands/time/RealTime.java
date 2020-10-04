package cosmos.commands.time;

import cosmos.Cosmos;
import cosmos.constants.ArgKeys;
import cosmos.constants.Outputs;
import cosmos.constants.Units;
import cosmos.listeners.time.RealTimeListener;
import cosmos.statics.arguments.Arguments;
import cosmos.statics.config.Config;
import cosmos.statics.finders.FinderWorldProperties;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import org.spongepowered.api.world.storage.WorldProperties;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("StaticVariableUsedBeforeInitialization")
public class RealTime extends AbstractTimeCommand {

    private static final Collection<UUID> computedWorlds = new ArrayList<>();
    private static UUID realTimeTaskUUID;

    public RealTime() {
        super(
                GenericArguments.optionalWeak(
                        GenericArguments.bool(ArgKeys.STATE.t)
                ),
                Arguments.baseFlag(ArgKeys.SAVE_CONFIG)
        );
    }

    public static void enableRealTimeFromConfig() {
        if (!Config.isListenerEnabled(RealTimeListener.class)) {
            cancelTask();
            computedWorlds.clear();
            return;
        }

        FinderWorldProperties.getAllWorldProperties(true)
                .stream()
                .filter(worldProperties -> !hasRealTimeTask(worldProperties))
                .filter(Config::isRealTimeWorldEnabled)
                .forEach(RealTime::enableRealTime);
    }

    public static void enableRealTime(WorldProperties worldProperties) {
        worldProperties.setGameRule(DefaultGameRules.DO_DAYLIGHT_CYCLE, "false");
        computedWorlds.add(worldProperties.getUniqueId());

        buildTaskIfNotExists();
    }

    public static boolean hasRealTimeTask(WorldProperties worldProperties) {
        return computedWorlds.contains(worldProperties.getUniqueId());
    }

    public static void manageRealTimeConflicts() {
        Sponge.getServer().getAllWorldProperties()
                .stream()
                .filter(worldProperties -> Config.isRealTimeWorldEnabled(worldProperties) && !Config.isListenerEnabled(RealTimeListener.class) ||
                        !Config.isRealTimeWorldEnabled(worldProperties) && hasRealTimeTask(worldProperties))
                .forEach(worldProperties -> worldProperties.setGameRule(DefaultGameRules.DO_DAYLIGHT_CYCLE, "true"));
    }

    private static void disableRealTime(WorldProperties worldProperties) {
        computedWorlds.remove(worldProperties.getUniqueId());
        worldProperties.setGameRule(DefaultGameRules.DO_DAYLIGHT_CYCLE, "true");

        cancelTaskIfEmpty();
    }

    private static void computeRealTime() {
        computedWorlds.stream()
                .map(uuid -> Sponge.getServer().getWorldProperties(uuid))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(RealTime::computeWorldRealTime);
    }

    private static void computeWorldRealTime(WorldProperties worldProperties) {
        long realTime = LocalTime.now(ZoneId.systemDefault()).toSecondOfDay();
        long pastMidnight = getPastMidnight(worldProperties.getWorldTime());

        long realTimeInTicks = realTime * Units.DAY_DURATION_IN_TICKS / Units.DAY_DURATION_IN_SECONDS;
        worldProperties.setWorldTime(pastMidnight + realTimeInTicks);
    }

    private static void buildTaskIfNotExists() {
        if (realTimeTaskUUID != null && Sponge.getScheduler().getTaskById(realTimeTaskUUID).isPresent()) {
            return;
        }

        Task.Builder realTimeTaskBuilder = Task.builder().execute(RealTime::computeRealTime);

        if (Config.isRealTimeSmooth()) {
            realTimeTaskBuilder.interval(Units.DAY_DURATION_IN_SECONDS / Units.DAY_DURATION_IN_TICKS, TimeUnit.SECONDS);
        } else {
            realTimeTaskBuilder.interval(1, TimeUnit.MINUTES);
        }

        realTimeTaskUUID = realTimeTaskBuilder
                .submit(Cosmos.instance)
                .getUniqueId();
    }

    private static void cancelTaskIfEmpty() {
        if (computedWorlds.isEmpty()) {
            cancelTask();
        }
    }

    public static void cancelTask() {
        if (realTimeTaskUUID != null) {
            Sponge.getScheduler().getTaskById(realTimeTaskUUID).ifPresent(Task::cancel);
        }
    }

    @Override
    protected void runWithProperties(CommandSource src, CommandContext args, WorldProperties worldProperties) throws CommandException {
        if (!Config.isListenerEnabled(RealTimeListener.class)) {
            throw Outputs.REALTIME_GLOBALLY_DISABLED.asException();
        }

        Optional<Boolean> optionalState = args.getOne(ArgKeys.STATE.t);
        boolean state = hasRealTimeTask(worldProperties);
        String mutableText = "currently";

        if (optionalState.isPresent()) {
            state = optionalState.get();
            mutableText = "successfully";
            if (state) {
                if (computedWorlds.contains(worldProperties.getUniqueId())) {
                    throw Outputs.REALTIME_ALREADY_ACTIVATED.asException(worldProperties);
                }

                enableRealTime(worldProperties);
            } else {
                if (!computedWorlds.contains(worldProperties.getUniqueId())) {
                    throw Outputs.REALTIME_ALREADY_DISABLED.asException(worldProperties);
                }

                disableRealTime(worldProperties);
            }
            FinderWorldProperties.saveProperties(worldProperties);
        }

        src.sendMessage(
                Outputs.REAL_TIME.asText(
                        worldProperties,
                        mutableText,
                        state ? "activated" : "disabled"
                )
        );

        if (!args.hasAny(ArgKeys.SAVE_CONFIG.t)) {
            return;
        }

        if (!Config.saveRealTime(worldProperties, state)) {
            src.sendMessage(Outputs.SAVING_CONFIG_NODE.asText("Real time"));
            return;
        }

        src.sendMessage(Outputs.SAVE_CONFIG_NODE.asText("Real time"));
    }
}
