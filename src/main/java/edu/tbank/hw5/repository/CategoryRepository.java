package edu.tbank.hw5.repository;

import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.storage.Storage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class CategoryRepository {
    private final Storage<Long, Category> storage;

    public List<Category> findAll() {
        log.info("Fetching all categories");
        return storage.getAll();
    }

    public Category findById(Long id) {
        log.info("Fetching category with id: {}", id);
        return storage.get(id);
    }

    public void save(Category category) {
        log.info("Saving category with id: {}", category.getId());
        storage.put(category.getId(), category);
    }

    public void deleteById(Long id) {
        log.info("Deleting category with id: {}", id);
        storage.remove(id);
    }

    public void saveAll(List<Category> categories) {
        log.info("Saving all categories");
        for (Category category : categories) {
            storage.put(category.getId(), category);
        }
    }

    public void update(Category category, Long id) {
        log.info("Updating category with id: {}", id);
        storage.put(id, category);
    }
}