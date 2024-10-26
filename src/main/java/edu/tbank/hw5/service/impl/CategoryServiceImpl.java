package edu.tbank.hw5.service.impl;

import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.memento.CategoryMemento;
import edu.tbank.hw5.repository.CategoryRepository;
import edu.tbank.hw5.repository.history.CategoryHistoryRepository;
import edu.tbank.hw5.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryHistoryRepository categoryHistoryRepository; // Внедряем репозиторий истории

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void updateCategoryById(Category category, Long id) {
        Category existingCategory = categoryRepository.findById(id);
        if (existingCategory != null) {
            saveMemento(existingCategory);
            categoryRepository.update(category, id);
        }
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category existingCategory = categoryRepository.findById(id);
        if (existingCategory != null) {
            saveMemento(existingCategory);
            categoryRepository.deleteById(id);
        }
    }

    private void saveMemento(Category category) {
        Long categoryId = category.getId();
        log.info("Saving memento with id {}", categoryId);
        CategoryMemento snapshot = new CategoryMemento(categoryId, category.getName(), category.getSlug());
        categoryHistoryRepository.save(categoryId, snapshot);
    }

    public List<CategoryMemento> getCategoryHistoryById(Long categoryId) {
        return categoryHistoryRepository.findByCategoryId(categoryId);
    }
}
