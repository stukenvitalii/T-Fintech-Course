package edu.tbank.hw5.repository.history;

import edu.tbank.hw5.memento.LocationMemento;
import edu.tbank.hw5.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryLocationHistoryRepository implements LocationHistoryRepository {
    private final Storage<String, List<LocationMemento>> historyStorage = new Storage<>();

    @Override
    public void save(String locationSlug, LocationMemento memento) {
        historyStorage.putIfAbsent(locationSlug, new ArrayList<>());
        historyStorage.get(locationSlug).add(memento);
    }

    @Override
    public List<LocationMemento> findByLocationSlug(String locationSlug) {
        return historyStorage.getOrDefault(locationSlug, new ArrayList<>());
    }
}
