package cosmos;


import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.lifecycle.ConstructPluginEvent;
import org.spongepowered.plugin.jvm.Plugin;

@Plugin(value = "cosmos")
public class Cosmos {

    public static Cosmos instance;

    @Listener
    public void onConstruction(ConstructPluginEvent event) {
        if (instance == null) instance = this;
    }
}