package cosmos.registries.perworld;

import com.google.inject.Singleton;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.util.Tuple;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Singleton
public class GroupRegistry implements CosmosRegistry<Tuple<String, ResourceKey>, Set<ResourceKey>> {

    private final Map<Tuple<String, ResourceKey>, Set<ResourceKey>> groupMap = new HashMap<>();

    @Override
    public Optional<CosmosRegistryEntry<Tuple<String, ResourceKey>, Set<ResourceKey>>> register(final Tuple<String, ResourceKey> key, final Set<ResourceKey> value) {
        return Optional.ofNullable(this.groupMap.computeIfAbsent(key, k -> value))
                .map(v -> {
                    value.forEach(k -> {
                        final Tuple<String, ResourceKey> tupleK = Tuple.of(key.first(), k);

                        if (!v.contains(k)) {
                            this.groupMap.put(tupleK, value);
                        }
                    });

                    v.forEach(k -> {
                        final Tuple<String, ResourceKey> tupleK = Tuple.of(key.first(), k);

                        if (!value.contains(k)) {
                            this.groupMap.remove(tupleK);
                        }
                    });

                    return CosmosRegistryEntry.of(key, this.groupMap.put(key, value));
                });
    }

    @Override
    public Optional<CosmosRegistryEntry<Tuple<String, ResourceKey>, Set<ResourceKey>>> unregister(final Tuple<String, ResourceKey> key) {
        return Optional.ofNullable(this.groupMap.remove(key))
                .map(v -> {
                    final Set<ResourceKey> value = new HashSet<>(v);
                    value.remove(key.second());

                    v.forEach(k -> {
                        final Tuple<String, ResourceKey> tupleK = Tuple.of(key.first(), k);

                        if (!tupleK.equals(key) && this.groupMap.containsKey(tupleK)) {
                            if (value.size() > 1) {
                                this.groupMap.put(tupleK, value);
                            } else {
                                this.groupMap.remove(tupleK);
                            }
                        }
                    });

                    return CosmosRegistryEntry.of(key, v);
                });
    }

    @Override
    public Set<ResourceKey> value(final Tuple<String, ResourceKey> key) {
        return this.groupMap.get(key);
    }

}
