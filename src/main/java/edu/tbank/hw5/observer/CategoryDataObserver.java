package edu.tbank.hw5.observer;

import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.repository.CategoryRepository;

import java.util.List;

public class CategoryDataObserver implements DataObserver<Category> {
    private final CategoryRepository categoryRepository;

    public CategoryDataObserver(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void update(List<Category> data) {
        categoryRepository.saveAll(data);
    }
}
