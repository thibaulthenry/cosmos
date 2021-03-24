package cosmos.services.registry.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
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
import cosmos.services.registry.RegistryProvider;

@Singleton
public class RegistryProviderImpl implements RegistryProvider {

    private final DataBuilderRegistry dataBuilderRegistry;
    private final FormatterRegistry formatterRegistry;
    private final GroupRegistry groupRegistry;
    private final IgnorePlayersSleepingRegistry ignorePlayersSleepingRegistry;
    private final ListenerRegistry listenerRegistry;
    private final PortalDispatcherRegistry portalDispatcherRegistry;
    private final PortalRegistry portalRegistry;
    private final PortalSelectionRegistry portalSelectionRegistry;
    private final PortalParticlesTaskRegistry portalParticlesTaskRegistry;
    private final PortalSoundAmbientTaskRegistry portalSoundAmbientTaskRegistry;
    private final PortalTeleportTaskRegistry portalTeleportTaskRegistry;
    private final PortalTypeRegistry portalTypeRegistry;
    private final RealTimeRegistry realTimeRegistry;
    private final ScoreboardRegistry scoreboardRegistry;
    private final SelectorTypeRegistry selectorTypeRegistry;

    @Inject
    public RegistryProviderImpl(final Injector injector) {
        this.dataBuilderRegistry = injector.getInstance(DataBuilderRegistry.class);
        this.formatterRegistry = injector.getInstance(FormatterRegistry.class);
        this.groupRegistry = injector.getInstance(GroupRegistry.class);
        this.ignorePlayersSleepingRegistry = injector.getInstance(IgnorePlayersSleepingRegistry.class);
        this.listenerRegistry = injector.getInstance(ListenerRegistry.class);
        this.portalDispatcherRegistry = injector.getInstance(PortalDispatcherRegistry.class);
        this.portalRegistry = injector.getInstance(PortalRegistry.class);
        this.portalSelectionRegistry = injector.getInstance(PortalSelectionRegistry.class);
        this.portalParticlesTaskRegistry = injector.getInstance(PortalParticlesTaskRegistry.class);
        this.portalSoundAmbientTaskRegistry = injector.getInstance(PortalSoundAmbientTaskRegistry.class);
        this.portalTeleportTaskRegistry = injector.getInstance(PortalTeleportTaskRegistry.class);
        this.portalTypeRegistry = injector.getInstance(PortalTypeRegistry.class);
        this.realTimeRegistry = injector.getInstance(RealTimeRegistry.class);
        this.scoreboardRegistry = injector.getInstance(ScoreboardRegistry.class);
        this.selectorTypeRegistry = injector.getInstance(SelectorTypeRegistry.class);
    }

    @Override
    public DataBuilderRegistry dataBuilder() {
        return this.dataBuilderRegistry;
    }

    @Override
    public FormatterRegistry formatter() {
        return this.formatterRegistry;
    }

    @Override
    public GroupRegistry group() {
        return this.groupRegistry;
    }

    @Override
    public IgnorePlayersSleepingRegistry ignorePlayersSleeping() {
        return this.ignorePlayersSleepingRegistry;
    }

    @Override
    public ListenerRegistry listener() {
        return this.listenerRegistry;
    }

    @Override
    public PortalRegistry portal() {
        return this.portalRegistry;
    }

    @Override
    public PortalDispatcherRegistry portalDispatcher() {
        return this.portalDispatcherRegistry;
    }

    @Override
    public PortalSelectionRegistry portalSelection() {
        return this.portalSelectionRegistry;
    }

    @Override
    public PortalParticlesTaskRegistry portalParticlesTask() {
        return this.portalParticlesTaskRegistry;
    }

    @Override
    public PortalSoundAmbientTaskRegistry portalSoundAmbientTask() {
        return this.portalSoundAmbientTaskRegistry;
    }

    @Override
    public PortalTeleportTaskRegistry portalTeleportTask() {
        return this.portalTeleportTaskRegistry;
    }

    @Override
    public PortalTypeRegistry portalType() {
        return this.portalTypeRegistry;
    }

    @Override
    public RealTimeRegistry realTime() {
        return this.realTimeRegistry;
    }

    @Override
    public ScoreboardRegistry scoreboards() {
        return this.scoreboardRegistry;
    }

    @Override
    public SelectorTypeRegistry selector() {
        return this.selectorTypeRegistry;
    }

}

