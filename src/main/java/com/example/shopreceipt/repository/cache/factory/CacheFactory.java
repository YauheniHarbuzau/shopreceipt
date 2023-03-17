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
        if ("LRU".equals(cacheType)) {
            return new LRUCache<>(cacheCapacity);
        } else if ("LFU".equals(cacheType)) {
            return new LFUCache<>(cacheCapacity);
        } else {
            throw new IllegalArgumentException("Only LRU or LFU cache algorithms");
        }
    }
}
