package org.yearup.data.mysql;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.CategoryDao;
import org.yearup.models.Category;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

@Component
public class MySqlCategoryDao extends MySqlDaoBase implements CategoryDao
{
    public MySqlCategoryDao(DataSource dataSource)
    {
        super(dataSource);
    }

    // get all categories logic
    @Override
    public List<Category> getAllCategories()
    {

        List<Category> categories = new ArrayList<>();

        String sql = "SELECT * FROM categories";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet row = statement.executeQuery();

            while (row.next())
            {
                Category category = mapRow(row);
                categories.add(category);
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }

        return categories;

    }

    @Override
    public Category getById(int id)
    {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int categoryId = resultSet.getInt("category_id");
                    String name = resultSet.getString("name");
                    String description = resultSet.getString("description");
                    Category category = new Category(categoryId, name, description);
                    return category;
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return null;
    }

    // creating a new category logic

    @Override
    public Category create(Category category)

    {

        String sql = "INSERT INTO categories (name, description) VALUES (?, ?);";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());


            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                // Retrieve the generated keys
                ResultSet generatedKeys = statement.getGeneratedKeys();

                if (generatedKeys.next()) {
                    // Retrieve the auto-incremented ID
                    int generatedId = generatedKeys.getInt(1);

                    // get the newly inserted category
                    category.setCategoryId(generatedId);
                }
            }
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return category;
    }

    // updating a category logic

    @Override
    public void update(int categoryId, Category category)

    {
        String sql = "UPDATE categories" +
                " SET name = ?, " +
                " description = ?" +
                " WHERE category_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDescription());
            statement.setInt(3, categoryId);


            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // deleting a category logic

    @Override
    public void delete(int categoryId)
    // delete category
    {

        String sql = "DELETE FROM categories " +
                " WHERE category_id = ?;";

        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, categoryId);

            statement.executeUpdate();
        }
        catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
    }

    // helper method used in getAllCategories method to prevent repetition

    private Category mapRow(ResultSet row) throws SQLException
    {
        int categoryId = row.getInt("category_id");
        String name = row.getString("name");
        String description = row.getString("description");

        Category category = new Category();

            category.setCategoryId(categoryId);
            category.setName(name);
            category.setDescription(description);

        return category;
    }

}
