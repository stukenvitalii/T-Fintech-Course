package edu.tbank.hw5.service.impl;

import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAll() {
        // Given
        Category category1 = new Category(1L,"slug1", "Category 1");
        Category category2 = new Category(2L,"slug2", "Category 2");
        List<Category> categories = Arrays.asList(category1, category2);

        when(categoryRepository.findAll()).thenReturn(categories);

        // When
        List<Category> result = categoryService.findAll();

        // Then
        assertEquals(2, result.size());
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testGetCategoryById() {
        // Given
        Category category = new Category(1L,"slug1", "Category 1");
        when(categoryRepository.findById(1L)).thenReturn(category);

        // When
        Category result = categoryService.getCategoryById(1L);

        // Then
        assertEquals("Category 1", result.getName());
        verify(categoryRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddCategory() {
        // Given
        Category category = new Category(1L,"slug", "New Category");

        // When
        categoryService.addCategory(category);

        // Then
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    public void testUpdateCategoryById() {
        // Given
        Category category = new Category(1L,"slug", "Updated Category");

        // When
        categoryService.updateCategoryById(category, 1L);

        // Then
        verify(categoryRepository, times(1)).update(category, 1L);
    }

    @Test
    public void testDeleteCategoryById() {
        // When
        categoryService.deleteCategoryById(1L);

        // Then
        verify(categoryRepository, times(1)).deleteById(1L);
    }
}
