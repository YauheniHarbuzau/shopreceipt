package com.example.shopreceipt.repository.cache;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * LRU cache algorithm
 */
public class LRUCache<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Map<K, V> cacheMap;
    private final Set<K> keySet;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new HashMap<>(capacity);
        this.keySet = new LinkedHashSet<>();
    }

    @Override
    public void put(K key, V value) {
        if (cacheMap.containsKey(key)) {
            cacheMap.remove(key);
            cacheMap.replace(key, value);
        } else if (cacheMap.size() >= this.capacity) {
            var keyToDelete = keySet.toArray()[0];
            keySet.remove(keyToDelete);
            cacheMap.remove(keyToDelete);
        }
        keySet.add(key);
        cacheMap.put(key, value);
    }

    @Override
    public Optional<V> get(K key) {
        V value = cacheMap.get(key);
        keySet.remove(key);
        keySet.add(key);
        return Optional.ofNullable(value);
    }

    @Override
    public void remove(K key) {
        cacheMap.remove(key);
        keySet.remove(key);
    }
}