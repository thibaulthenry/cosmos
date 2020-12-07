package cosmos.registries;

public interface CosmosRegistry<K, V> {

    V get(K key);

    boolean has(K key);

}
