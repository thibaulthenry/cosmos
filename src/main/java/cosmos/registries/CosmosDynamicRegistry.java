package cosmos.registries;

public interface CosmosDynamicRegistry<K, V> extends CosmosRegistry<K, V> {

    void put(K key, V value);

}
