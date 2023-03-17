package com.example.shopreceipt.repository.cache;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * LFU cache algorithm
 */
public class LFUCache<K, V> implements Cache<K, V> {

    private final int capacity;
    private final Map<K, V> cacheMap;
    private final Set<K> keySet;
    private final Map<K, Integer> countMap;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        this.cacheMap = new HashMap<>(capacity);
        this.keySet = new LinkedHashSet<>();
        this.countMap = new LinkedHashMap<>();
    }

    @Override
    public void put(K key, V value) {
        if (cacheMap.containsKey(key)) {
            var freqs = countMap.get(key);
            countMap.replace(key, freqs + 1);
            keySet.remove(key);
            cacheMap.replace(key, value);
        } else {
            if (cacheMap.size() >= this.capacity) {
                var keyToDelete = countMap.entrySet().stream()
                        .sorted(Comparator.comparingInt(Map.Entry::getValue))
                        .map(Map.Entry::getKey).findFirst().get();
                remove(keyToDelete);
            }
            countMap.put(key, 1);
            cacheMap.put(key, value);
        }
        keySet.add(key);
    }

    @Override
    public Optional<V> get(K key) {
        V value = cacheMap.get(key);
        var freqs = countMap.get(key);
        if (freqs != null) {
            countMap.replace(key, freqs + 1);
        } else {
            countMap.put(key, 1);
        }
        keySet.remove(key);
        keySet.add(key);
        return Optional.ofNullable(value);
    }

    @Override
    public void remove(K key) {
        cacheMap.remove(key);
        keySet.remove(key);
        countMap.remove(key);
    }
}
