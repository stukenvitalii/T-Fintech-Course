package edu.tbank.hw5.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Storage<K, V> {
    private final ConcurrentMap<K, V> storage = new ConcurrentHashMap<>();

    public List<V> getAll() {
        return new ArrayList<>(storage.values());
    }

    public V get(K key) {
        return storage.get(key);
    }

    public void put(K key, V value) {
        storage.put(key, value);
    }

    public void remove(K key) {
        storage.remove(key);
    }

    public int size() {
        return storage.size();
    }

    public void putIfAbsent(K key, V value) {
        storage.putIfAbsent(key, value);
    }

    public V getOrDefault(K key, V defaultValue) {
        return storage.getOrDefault(key, defaultValue);
    }
}
