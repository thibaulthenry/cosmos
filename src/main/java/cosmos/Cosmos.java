package cosmos;

import com.google.inject.Inject;
import cosmos.commands.CommandRegister;
import cosmos.utils.Finder;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "cosmos",
        name = "Cosmos",
        version = "alpha",
        description = "Cosmos | Worlds management plugin"
)
public class Cosmos {

    private static Cosmos instance;

    @Inject
    private Logger logger;

    public static Cosmos getInstance() {
        return instance;
    }

    @Listener
    @SuppressWarnings("unused")
    public void onInitialization(GameInitializationEvent event) {
        instance = this;

        Sponge.getCommandManager().register(this, new CommandRegister().getCommandSpec(), "cosmos", "cm");

        if (!Finder.initBackupsFolders()) {
            sendConsole("An error occurred while initializing backups folder");
        }
    }

    @Listener
    @SuppressWarnings("unused")
    public void onServerStarting(GameStartingServerEvent event) {
    }

    public Logger getLogger() {
        return logger;
    }

    public void sendConsole(String text) {
        Sponge.getServer().getConsole().sendMessage(Text.of("[COSMOS]: ", text));
    }
}
