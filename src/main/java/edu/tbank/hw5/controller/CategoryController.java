package edu.tbank.hw5.controller;

import edu.tbank.hw5.entity.Category;
import edu.tbank.hw5.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.tinkoff.measurementloggingstarter.annotation.Timed;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/place-categories")
@Timed
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAll();
    }

    @GetMapping("/{id}")
    public Category getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/add")
    public void addCategory(@RequestBody Category category) {
        categoryService.addCategory(category);
    }

    @PutMapping("/update/{id}")
    public void updateCategoryById(@RequestBody Category category, @PathVariable Long id) {
        categoryService.updateCategoryById(category, id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
    }
}
