package org.yearup.data;

import org.yearup.models.Category;

import java.util.List;

//interface that holds vital methods to be used by the MySqlCategoryDao

public interface CategoryDao
{
    List<Category> getAllCategories();
    Category getById(int categoryId);
    Category create(Category category);
    void update(int categoryId, Category category);
    void delete(int categoryId);
}
