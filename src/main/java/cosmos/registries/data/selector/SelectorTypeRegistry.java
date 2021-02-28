package cosmos.registries.data.selector;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.DataKeys;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.registries.data.selector.impl.WorldSelectorType;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.selector.SelectorType;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Singleton
public class SelectorTypeRegistry implements CosmosRegistry<ResourceKey, SelectorType> {

    private final Map<ResourceKey, SelectorType> selectorTypeMap = new HashMap<>();

    @Inject
    public SelectorTypeRegistry(final Injector injector) {
        this.selectorTypeMap.put(DataKeys.SELECTOR_WORLD, injector.getInstance(WorldSelectorType.class));
    }

    public Stream<CosmosRegistryEntry<ResourceKey, SelectorType>> streamEntries() {
        return this.selectorTypeMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public SelectorType value(final ResourceKey key) {
        return this.selectorTypeMap.get(key);
    }

}
