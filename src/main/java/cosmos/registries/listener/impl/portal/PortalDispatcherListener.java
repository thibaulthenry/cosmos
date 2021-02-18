package cosmos.registries.listener.impl.portal;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.listener.impl.AbstractListener;
import cosmos.registries.portal.PortalDispatcherRegistry;
import org.spongepowered.api.event.EventContextKeys;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.cause.entity.MovementType;
import org.spongepowered.api.event.cause.entity.MovementTypes;
import org.spongepowered.api.event.entity.ChangeEntityWorldEvent;
import org.spongepowered.api.event.filter.IsCancelled;
import org.spongepowered.api.util.Tristate;
import org.spongepowered.api.world.portal.PortalType;

import java.util.Optional;

@Singleton
public class PortalDispatcherListener extends AbstractListener {

    private final PortalDispatcherRegistry portalDispatcherRegistry;

    @Inject
    public PortalDispatcherListener(final Injector injector) {
        this.portalDispatcherRegistry = injector.getInstance(PortalDispatcherRegistry.class);
    }

    @Listener
    @IsCancelled(Tristate.FALSE)
    public void onPreChangeEntityWorldEvent(final ChangeEntityWorldEvent.Pre event) {
        final Optional<MovementType> optionalMovementType = event.context().get(EventContextKeys.MOVEMENT_TYPE);

        if (!optionalMovementType.isPresent() || !MovementTypes.PORTAL.get().equals(optionalMovementType.get())) {
            return;
        }

        final Optional<PortalType> optionalPortalType = event.cause().first(PortalType.class);

        if (!optionalPortalType.isPresent()) {
            return;
        }

        // TODO https://github.com/SpongePowered/Sponge/pull/3344

        this.portalDispatcherRegistry.find(event.originalWorld().key())
                .flatMap(portalDispatcher -> portalDispatcher.findLinkedWorld(optionalPortalType.get()))
                .ifPresent(event::setDestinationWorld);
    }

}
