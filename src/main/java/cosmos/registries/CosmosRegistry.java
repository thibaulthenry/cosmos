package cosmos.registries;

import java.util.Objects;
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
        Objects.requireNonNull(key, "key");
        Objects.requireNonNull(value, "value");

        return Optional.empty();
    }

    default Stream<V> stream() {
        return Stream.empty();
    }

    default Stream<CosmosRegistryEntry<K, V>> streamEntries() {
        return Stream.empty();
    }

    default Optional<CosmosRegistryEntry<K, V>> unregister(final K key) {
        Objects.requireNonNull(key, "key");

        return Optional.empty();
    }

    V value(K key);

}
