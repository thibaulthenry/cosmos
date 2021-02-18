package cosmos.registries;

public interface CosmosRegistryEntry<K, V> {

    static <K, V> CosmosRegistryEntry<K, V> of(K key, V value) {
        return new CosmosRegistryEntryImpl<>(key, value);
    }

    K key();

    V value();

}
