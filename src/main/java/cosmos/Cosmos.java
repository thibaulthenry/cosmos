package cosmos;

import cosmos.commands.time.IgnorePlayersSleeping;
import cosmos.commands.time.RealTime;
import cosmos.listeners.ListenerRegister;
import cosmos.modules.Root;
import cosmos.statics.config.Config;
import cosmos.statics.finders.FinderFile;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;

@Plugin(
        id = "cosmos",
        name = "Cosmos",
        version = "1.0.1-7.3.0",
        description = "Cosmos | Worlds management | Per-world management",
        url = "https://ore.spongepowered.org/Kazz96/Cosmos",
        authors = "Kazz"
)
@SuppressWarnings("MethodMayBeStatic")
public class Cosmos {

    public static Cosmos instance;

    public static void sendConsole(Object... values) {
        Arrays.stream(values)
                .map(value -> Text.of("[COSMOS]: ", value))
                .forEach(text -> Sponge.getServer().getConsole().sendMessages(text));
    }

    @Listener
    public void onConstruction(GameConstructionEvent event) {
        if (instance == null) instance = this;
    }

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) {
        Config.load();
    }

    @Listener
    public void onInitialization(GameInitializationEvent event) {
        Sponge.getCommandManager().register(this, new Root().getCommandSpec(), "cosmos", "cm");
        ListenerRegister.initializeListeners();

        if (!FinderFile.initDirectories()) {
            sendConsole(Text.of(TextColors.RED, "An error occurred while initializing Cosmos directories"));
        }
    }

    @Listener
    public void onGameStarting(GameStartingServerEvent event) {
        IgnorePlayersSleeping.enableSleepIgnoranceFromConfig();
        RealTime.enableRealTimeFromConfig();
    }

    @Listener
    public void onServerStopping(GameStoppingServerEvent event) {
        RealTime.manageRealTimeConflicts();
    }

    @Listener
    public void onGameStopping(GameStoppingEvent event) {
        Config.save();
        RealTime.cancelTask();
        ListenerRegister.cancelScheduledSaveTask();
    }

    @Listener
    public void onReload(GameReloadEvent event) {
        ListenerRegister.unregisterAll();
        Config.load();
        RealTime.manageRealTimeConflicts();
        ListenerRegister.initializeListeners();
        IgnorePlayersSleeping.enableSleepIgnoranceFromConfig();
        RealTime.enableRealTimeFromConfig();
    }
}