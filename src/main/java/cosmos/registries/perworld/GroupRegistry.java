package cosmos.registries.perworld;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import cosmos.Cosmos;
import cosmos.constants.ConfigurationNodes;
import cosmos.constants.PerWorldFeatures;
import cosmos.registries.CosmosRegistry;
import cosmos.registries.CosmosRegistryEntry;
import cosmos.services.io.ConfigurationService;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.util.Tuple;
import org.spongepowered.configurate.ConfigurationNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class GroupRegistry implements CosmosRegistry<Tuple<PerWorldFeatures, ResourceKey>, Set<ResourceKey>> {

    private final Map<Tuple<PerWorldFeatures, ResourceKey>, Set<ResourceKey>> groupMap = new HashMap<>();

    private final ConfigurationService configurationService;

    @Inject
    public GroupRegistry(final Injector injector) {
        this.configurationService = injector.getInstance(ConfigurationService.class);
    }

    @Override
    public Optional<CosmosRegistryEntry<Tuple<PerWorldFeatures, ResourceKey>, Set<ResourceKey>>> register(final Tuple<PerWorldFeatures, ResourceKey> key, final Set<ResourceKey> value) {
        return Optional.ofNullable(this.groupMap.computeIfAbsent(key, k -> value))
                .map(v -> {
                    value.forEach(k -> {
                        final Tuple<PerWorldFeatures, ResourceKey> tupleK = Tuple.of(key.first(), k);

                        if (!v.contains(k)) {
                            this.groupMap.put(tupleK, value);
                        }
                    });

                    v.forEach(k -> {
                        final Tuple<PerWorldFeatures, ResourceKey> tupleK = Tuple.of(key.first(), k);

                        if (!value.contains(k)) {
                            this.groupMap.remove(tupleK);
                        }
                    });

                    return CosmosRegistryEntry.of(key, this.groupMap.put(key, value));
                });
    }

    public void registerAll() {
        if (!this.configurationService.isLoaded()) {
            Cosmos.logger().error("Configuration file not loaded. No per world groups loaded !");
            return;
        }

        for (final PerWorldFeatures feature : PerWorldFeatures.values()) {
            this.configurationService
                    .findNode(ConfigurationNodes.PER_WORLD, feature.formatted(), ConfigurationNodes.PER_WORLD_GROUPS)
                    .map(ConfigurationNode::childrenList)
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(ConfigurationNode::childrenList)
                    .map(nodes -> nodes.stream()
                            .map(ConfigurationNode::getString)
                            .map(formatted -> {
                                try {
                                    final ResourceKey key = ResourceKey.resolve(formatted);

                                    if (Sponge.server().worldManager().worldExists(key)) {
                                        return Optional.ofNullable(key);
                                    }
                                } catch (final Exception ignored) { }

                                Cosmos.logger().warn("Impossible to add element " + formatted + " from " + feature.formatted() + " per-world groups");

                                return Optional.<ResourceKey>empty();
                            })
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toSet()))
                    .forEach(group -> group.forEach(key -> this.groupMap.putIfAbsent(Tuple.of(feature, key), group)));

            this.streamEntries().forEach(entry -> this.register(entry.key(), entry.value()));
        }
    }

    @Override
    public Stream<CosmosRegistryEntry<Tuple<PerWorldFeatures, ResourceKey>, Set<ResourceKey>>> streamEntries() {
        return this.groupMap.entrySet().stream().map(entry -> CosmosRegistryEntry.of(entry.getKey(), entry.getValue()));
    }

    @Override
    public Optional<CosmosRegistryEntry<Tuple<PerWorldFeatures, ResourceKey>, Set<ResourceKey>>> unregister(final Tuple<PerWorldFeatures, ResourceKey> key) {
        return Optional.ofNullable(this.groupMap.remove(key))
                .map(v -> {
                    final Set<ResourceKey> value = new HashSet<>(v);
                    value.remove(key.second());

                    v.forEach(k -> {
                        final Tuple<PerWorldFeatures, ResourceKey> tupleK = Tuple.of(key.first(), k);

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
    public Set<ResourceKey> value(final Tuple<PerWorldFeatures, ResourceKey> key) {
        return this.groupMap.get(key);
    }

}
