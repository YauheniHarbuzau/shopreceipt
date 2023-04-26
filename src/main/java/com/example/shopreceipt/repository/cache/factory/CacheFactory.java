package com.example.shopreceipt.repository.cache.factory;

import com.example.shopreceipt.repository.cache.Cache;
import com.example.shopreceipt.repository.cache.LFUCache;
import com.example.shopreceipt.repository.cache.LRUCache;

/**
 * Factory for the cache
 *
 * @see LRUCache
 * @see LFUCache
 */
public class CacheFactory<K, V> implements Factory<K, V> {

    @Override
    public Cache<K, V> initCache(String cacheType, Integer cacheCapacity) {
        return switch (cacheType) {
            case "LRU" -> new LRUCache<>(cacheCapacity);
            case "LFU" -> new LFUCache<>(cacheCapacity);
            default -> throw new IllegalArgumentException("Only LRU or LFU cache algorithms");
        };
    }
}
