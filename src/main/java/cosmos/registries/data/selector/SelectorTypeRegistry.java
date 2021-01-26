package cosmos.registries.data.selector;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.constants.DataKeys;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.data.selector.impl.WorldSelectorType;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.selector.SelectorType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class SelectorTypeRegistry implements CosmosRegistry<ResourceKey, SelectorType> {

    private final Map<ResourceKey, SelectorType> selectorTypeMap = new HashMap<>();

    @Inject
    public SelectorTypeRegistry(final Injector injector) {
        this.selectorTypeMap.put(DataKeys.SELECTOR_WORLD, injector.getInstance(WorldSelectorType.class));
    }

    public Set<Map.Entry<ResourceKey, SelectorType>> entries() {
        return this.selectorTypeMap.entrySet();
    }

    @Override
    public SelectorType get(final ResourceKey key) {
        return this.selectorTypeMap.get(key);
    }

    @Override
    public boolean has(final ResourceKey key) {
        return this.selectorTypeMap.containsKey(key);
    }

}
