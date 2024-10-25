package edu.tbank.hw5.repository;

import edu.tbank.hw5.dto.Category;
import edu.tbank.hw5.storage.Storage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = CategoryRepository.class)
public class CategoryRepositoryTest {

    @MockBean
    private Storage<Long, Category> storage;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category1;
    private Category category2;

    @BeforeEach
    public void setup() {
        category1 = new Category(1L,"slug1", "Category 1");
        category2 = new Category(2L,"slug2", "Category 2");
    }

    @Test
    public void testFindAll() {
        // Given
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);
        when(storage.getAll()).thenReturn(categories);

        // When
        List<Category> result = categoryRepository.findAll();

        // Then
        assertEquals(2, result.size());
        assertEquals(categories, result);
        verify(storage).getAll();
    }

    @Test
    public void testFindById() {
        // Given
        when(storage.get(1L)).thenReturn(category1);

        // When
        Category result = categoryRepository.findById(1L);

        // Then
        assertNotNull(result);
        assertEquals(category1, result);
        verify(storage).get(1L);
    }

    @Test
    public void testSave() {
        // When
        categoryRepository.save(category1);

        // Then
        verify(storage).put(1L, category1);
    }

    @Test
    public void testSaveAll() {
        // Given
        List<Category> categories = new ArrayList<>();
        categories.add(category1);
        categories.add(category2);

        // When
        categoryRepository.saveAll(categories);

        // Then
        verify(storage).put(1L, category1);
        verify(storage).put(2L, category2);
    }

    @Test
    public void testUpdate() {
        // When
        categoryRepository.update(category1, 1L);

        // Then
        verify(storage).put(1L, category1);
    }

    @Test
    public void testDeleteById() {
        // When
        categoryRepository.deleteById(1L);

        // Then
        verify(storage).remove(1L);
    }
}