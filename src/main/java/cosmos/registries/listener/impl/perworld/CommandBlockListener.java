package cosmos.registries.listener.impl.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.PerWorldFeatures;
import cosmos.registries.perworld.GroupRegistry;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandCause;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.command.ExecuteCommandEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.api.world.server.ServerLocation;
import org.spongepowered.api.world.server.ServerWorld;

import java.util.Collections;
import java.util.Optional;

@Singleton
public class CommandBlockListener extends AbstractPerWorldListener {

    private final GroupRegistry groupRegistry;

    @Inject
    public CommandBlockListener(final Injector injector) {
        this.groupRegistry = injector.getInstance(GroupRegistry.class);
    }

    @Listener(order = Order.FIRST)
    @IsCancelled(Tristate.FALSE)
    public void onPreExecuteCommandEvent(final ExecuteCommandEvent.Pre event, @First final CommandCause cause) {
        final Optional<ServerWorld> optionalWorld = cause.location().map(ServerLocation::world);

        if (!optionalWorld.isPresent()) {
            return;
        }

        final ServerWorld world = optionalWorld.get();

        final ResourceKey key = this.groupRegistry.find(Tuple.of(PerWorldFeatures.COMMAND_BLOCK, world.key()))
                .orElse(Collections.singleton(world.key()))
                .iterator()
                .next();

        if (!Sponge.server().worldManager().world(key).orElse(world).properties().commands()) {
            event.setCancelled(true);
        }
    }

}
