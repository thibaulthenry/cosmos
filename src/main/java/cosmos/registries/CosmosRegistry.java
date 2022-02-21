package cosmos.registries;

import cosmos.Cosmos;

import java.util.Optional;
import java.util.stream.Stream;

public interface CosmosRegistry<K, V> {

    default Optional<V> find(final K key) {
        return Optional.ofNullable(this.value(key));
    }

    default boolean has(final K key) {
        return this.find(key).isPresent();
    }

    default Optional<CosmosRegistryEntry<K, V>> register(final K key, final V value) {
        Cosmos.logger().warn("CosmosRegistry#register not implemented for: " + this.getClass().getSimpleName());
        return Optional.empty();
    }

    default Stream<V> stream() {
        Cosmos.logger().warn("CosmosRegistry#stream not implemented for: " + this.getClass().getSimpleName());
        return Stream.empty();
    }

    default Stream<CosmosRegistryEntry<K, V>> streamEntries() {
        Cosmos.logger().warn("CosmosRegistry#streamEntries not implemented for: " + this.getClass().getSimpleName());
        return Stream.empty();
    }

    default Optional<CosmosRegistryEntry<K, V>> unregister(final K key) {
        Cosmos.logger().warn("CosmosRegistry#unregister not implemented for: " + this.getClass().getSimpleName());
        return Optional.empty();
    }

    V value(K key);

}
