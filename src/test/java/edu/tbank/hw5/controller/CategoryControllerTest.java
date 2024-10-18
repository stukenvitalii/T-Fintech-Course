package edu.tbank.hw5.controller;

import edu.tbank.hw5.entity.Category;
import edu.tbank.hw5.service.CategoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CategoryController.class})
class CategoryControllerTest {

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Get all categories returns list of categories")
    void getAllCategories_returnsListOfCategories() throws Exception {
        List<Category> categories = Arrays.asList(
                new Category(1L,"slug1","name1"),
                new Category(2L,"slug2","name2"));
        when(categoryService.findAll()).thenReturn(categories);

        mockMvc.perform(get("/place-categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categories.size()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Get category by ID returns category")
    void getCategoryById_returnsCategory() throws Exception {
        Category category = new Category(1L, "slug","name");
        when(categoryService.getCategoryById(1L)).thenReturn(category);

        mockMvc.perform(get("/place-categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(category.getId()))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Add category creates new category")
    void addCategory_createsNewCategory() throws Exception {
        Category category = new Category();
        String categoryJson = "{\"name\":\"New Category\"}";

        mockMvc.perform(post("/place-categories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(categoryService, times(1)).addCategory(any(Category.class));
    }

    @Test
    @DisplayName("Update category by ID updates existing category")
    void updateCategoryById_updatesExistingCategory() throws Exception {
        Category category = new Category();
        String categoryJson = "{\"name\":\"Updated Category\"}";

        mockMvc.perform(put("/place-categories/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(categoryJson))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(categoryService, times(1)).updateCategoryById(any(Category.class), eq(1L));
    }

    @Test
    @DisplayName("Delete category by ID deletes category")
    void deleteCategoryById_deletesCategory() throws Exception {
        mockMvc.perform(delete("/place-categories/delete/1"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(categoryService, times(1)).deleteCategoryById(1L);
    }
}