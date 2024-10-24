package edu.tbank.hw5.repository;

import edu.tbank.hw5.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CategoryRepository extends BaseRepository<Long, Category> {

    public void save(Category category) {
        save(category, category.getId());
    }

    public void saveAll(List<Category> categories) {
        List<Long> ids = categories.stream().map(Category::getId).collect(Collectors.toList());
        saveAll(categories, ids);
    }
}