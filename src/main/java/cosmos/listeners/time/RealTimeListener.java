package cosmos.listeners.time;

import cosmos.commands.time.RealTime;
import cosmos.constants.Outputs;
import cosmos.listeners.AbstractListener;
import cosmos.statics.config.Config;
import org.spongepowered.api.GameState;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.world.ChangeWorldGameRuleEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.text.channel.MessageReceiver;
import org.spongepowered.api.world.gamerule.DefaultGameRules;
import org.spongepowered.api.world.storage.WorldProperties;

public class RealTimeListener extends AbstractListener {

    @Listener
    public void onWorldLoad(LoadWorldEvent event) {
        WorldProperties worldProperties = event.getTargetWorld().getProperties();

        if (Config.isRealTimeWorldEnabled(worldProperties) && !RealTime.hasRealTimeTask(worldProperties)) {
            RealTime.enableRealTime(worldProperties);
        }
    }

    @Listener
    public void onDayLightCycleGameRuleChange(ChangeWorldGameRuleEvent event) {
        if (GameState.SERVER_STOPPING.equals(Sponge.getGame().getState())) {
            return;
        }

        if (!DefaultGameRules.DO_DAYLIGHT_CYCLE.equals(event.getName()) || event.getValue().contains("false")) {
            return;
        }

        if (!RealTime.hasRealTimeTask(event.getTargetWorld().getProperties())) {
            return;
        }

        event.getCause().first(MessageReceiver.class).ifPresent(messageReceiver ->
                messageReceiver.sendMessage(Outputs.REALTIME_DAY_LIGHT_CYCLE_LOCKED.asText(event.getTargetWorld().getName()))
        );

        event.setCancelled(true);
    }
}
