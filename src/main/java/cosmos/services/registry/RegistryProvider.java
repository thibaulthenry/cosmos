package cosmos.services.registry;

import com.google.inject.ImplementedBy;
import cosmos.registries.data.builder.DataBuilderRegistry;
import cosmos.registries.data.portal.PortalTypeRegistry;
import cosmos.registries.data.selector.SelectorTypeRegistry;
import cosmos.registries.formatter.FormatterRegistry;
import cosmos.registries.listener.ListenerRegistry;
import cosmos.registries.perworld.GroupRegistry;
import cosmos.registries.perworld.IgnorePlayersSleepingRegistry;
import cosmos.registries.perworld.RealTimeRegistry;
import cosmos.registries.perworld.ScoreboardRegistry;
import cosmos.registries.portal.PortalDispatcherRegistry;
import cosmos.registries.portal.PortalParticlesTaskRegistry;
import cosmos.registries.portal.PortalRegistry;
import cosmos.registries.portal.PortalSelectionRegistry;
import cosmos.registries.portal.PortalSoundAmbientTaskRegistry;
import cosmos.registries.portal.PortalTeleportTaskRegistry;
import cosmos.services.CosmosService;
import cosmos.services.registry.impl.RegistryProviderImpl;

@ImplementedBy(RegistryProviderImpl.class)
public interface RegistryProvider extends CosmosService {

    DataBuilderRegistry dataBuilder();

    FormatterRegistry formatter();

    GroupRegistry group();

    IgnorePlayersSleepingRegistry ignorePlayersSleeping();

    ListenerRegistry listener();

    PortalRegistry portal();

    PortalDispatcherRegistry portalDispatcher();

    PortalSelectionRegistry portalSelection();

    PortalParticlesTaskRegistry portalParticlesTask();

    PortalSoundAmbientTaskRegistry portalSoundAmbientTask();

    PortalTeleportTaskRegistry portalTeleportTask();

    PortalTypeRegistry portalType();

    RealTimeRegistry realTime();

    ScoreboardRegistry scoreboard();

    SelectorTypeRegistry selector();

}
