package edu.tbank.hw5.repository.history;


import edu.tbank.hw5.memento.CategoryMemento;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryHistoryRepository {
    void save(Long categoryId, CategoryMemento memento);
    List<CategoryMemento> findByCategoryId(Long categoryId);
}