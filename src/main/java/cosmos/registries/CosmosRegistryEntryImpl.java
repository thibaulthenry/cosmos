package cosmos.registries;

class CosmosRegistryEntryImpl<K, V> implements CosmosRegistryEntry<K, V> {

    private final K key;
    private final V value;

    CosmosRegistryEntryImpl(final K key, final V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K key() {
        return this.key;
    }

    @Override
    public V value() {
        return this.value;
    }

}
