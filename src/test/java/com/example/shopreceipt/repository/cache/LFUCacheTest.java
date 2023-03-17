package com.example.shopreceipt.repository.cache;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for the {@link LFUCache}
 */
class LFUCacheTest {

    private static Cache<Long, Integer> cache;
    private static int capacity;

    @Test
    void checkPut() {
        capacity = 2;
        cache = new LFUCache<>(capacity);

        cache.put(1L, 10);
        cache.get(1L);
        cache.put(2L, 20);
        cache.put(3L, 30);

        assertAll(
                () -> assertTrue(cache.get(1L).isPresent()),
                () -> assertFalse(cache.get(2L).isPresent()),
                () -> assertTrue(cache.get(3L).isPresent())
        );
    }

    @Nested
    class GetTest {
        @Test
        void checkGet1() {
            capacity = 2;
            cache = new LFUCache<>(capacity);

            cache.put(1L, 10);
            cache.get(1L);
            cache.put(2L, 20);
            cache.put(3L, 30);

            assertAll(
                    () -> assertEquals(Optional.of(10), cache.get(1L)),
                    () -> assertNotEquals(Optional.of(20), cache.get(2L)),
                    () -> assertEquals(Optional.of(30), cache.get(3L))
            );
        }

        @Test
        void checkGet2() {
            capacity = 1;
            cache = new LFUCache<>(capacity);

            assertEquals(Optional.empty(), cache.get(1L));

            cache.put(1L, 10);
            assertEquals(Optional.of(10), cache.get(1L));
        }
    }

    @Test
    void checkRemove() {
        capacity = 3;
        cache = new LFUCache<>(capacity);

        cache.put(1L, 10);
        cache.put(2L, 20);
        cache.put(3L, 30);
        cache.remove(3L);

        assertAll(
                () -> assertTrue(cache.get(1L).isPresent()),
                () -> assertTrue(cache.get(2L).isPresent()),
                () -> assertFalse(cache.get(3L).isPresent())
        );
    }
}
