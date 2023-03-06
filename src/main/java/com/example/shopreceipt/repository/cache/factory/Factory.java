package com.example.shopreceipt.repository.cache.factory;

import com.example.shopreceipt.repository.cache.Cache;

/**
 * Interface for the {@link CacheFactory}
 */
public interface Factory<K, V> {

    Cache<K, V> initCache(String cacheType, Integer cacheCapacity);
}