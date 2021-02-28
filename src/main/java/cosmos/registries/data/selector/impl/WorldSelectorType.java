package cosmos.registries.data.selector.impl;

import com.google.inject.Singleton;
import cosmos.Cosmos;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.command.selector.Selector;
import org.spongepowered.api.command.selector.SelectorType;

@Singleton
public class WorldSelectorType implements SelectorType {

    private final ResourceKey key = ResourceKey.of(Cosmos.NAMESPACE, "all_worlds");

    @Override
    public String selectorToken() {
        return "w";
    }

    @Override
    public Selector toSelector() {
        return Selector.builder().build();
    }

    @Override
    public Selector.Builder toBuilder() {
        return Selector.builder();
    }

}
