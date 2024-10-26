package edu.tbank.hw5.repository.history;

import edu.tbank.hw5.memento.CategoryMemento;
import edu.tbank.hw5.storage.Storage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class InMemoryCategoryHistoryRepository implements CategoryHistoryRepository {
    private final Storage<Long, List<CategoryMemento>> historyStorage = new Storage<>();

    @Override
    public void save(Long categoryId, CategoryMemento memento) {
        historyStorage.putIfAbsent(categoryId, new ArrayList<>());
        historyStorage.get(categoryId).add(memento);
    }

    @Override
    public List<CategoryMemento> findByCategoryId(Long categoryId) {
        return historyStorage.getOrDefault(categoryId, new ArrayList<>());
    }
}
