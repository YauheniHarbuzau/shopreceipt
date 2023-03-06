package com.example.shopreceipt.repository.cache;

import java.util.Optional;

/**
 * Interface for the cache
 *
 * @see LRUCache
 * @see LFUCache
 */
public interface Cache<K, V> {

    void put(K key, V value);

    Optional<V> get(K key);

    void remove(K key);
}