package cosmos.services.data.selector.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.registries.data.selector.SelectorTypeRegistry;
import cosmos.services.data.selector.SelectorService;
import org.spongepowered.api.command.selector.SelectorType;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryTypes;

@Singleton
public class SelectorServiceImpl implements SelectorService {

    private final SelectorTypeRegistry selectorTypeRegistry;

    @Inject
    public SelectorServiceImpl(final Injector injector) {
        this.selectorTypeRegistry = injector.getInstance(SelectorTypeRegistry.class);
    }

    @Override
    public void registerAll() {
        final Registry<SelectorType> registry = RegistryTypes.SELECTOR_TYPE.get();
        this.selectorTypeRegistry.entries().forEach(entry -> registry.register(entry.getKey(), entry.getValue()));
    }

}
