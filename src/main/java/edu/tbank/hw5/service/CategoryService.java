package edu.tbank.hw5.service;

import edu.tbank.hw5.entity.Category;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CategoryService {
    List<Category> findAll();
    Category getCategoryById(Long id);
    void addCategory(Category category);
    void updateCategoryById(Category category, Long id);
    void deleteCategoryById(Long id);
}