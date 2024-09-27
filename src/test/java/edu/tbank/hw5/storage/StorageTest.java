package edu.tbank.hw5.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StorageTest {

    private Storage<Long, String> storage;

    @BeforeEach
    void setUp() {
        storage = new Storage<>();
    }

    @Test
    void getAll_returnsAllValues() {
        storage.put(1L, "value1");
        storage.put(2L, "value2");

        List<String> result = storage.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains("value1"));
        assertTrue(result.contains("value2"));
    }

    @Test
    void get_returnsValueForKey() {
        storage.put(1L, "value1");

        String result = storage.get(1L);

        assertEquals("value1", result);
    }

    @Test
    void get_returnsNullForNonExistentKey() {
        String result = storage.get(1L);

        assertNull(result);
    }

    @Test
    void put_addsValue() {
        storage.put(1L, "value1");

        assertEquals("value1", storage.get(1L));
    }

    @Test
    void remove_deletesValue() {
        storage.put(1L, "value1");
        storage.remove(1L);

        assertNull(storage.get(1L));
    }

    @Test
    void size_returnsNumberOfEntries() {
        storage.put(1L, "value1");
        storage.put(2L, "value2");

        assertEquals(2, storage.size());
    }

    @Test
    void size_returnsZeroWhenEmpty() {
        assertEquals(0, storage.size());
    }
}