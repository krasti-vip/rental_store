package ru.rental.servic.dao;

import ru.rental.servic.model.User;
import ru.rental.servic.util.PropertiesUtil;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

public class UserDao implements DAO<User, String> {

    private static final String SELECT_USER = """
            SELECT * FROM users WHERE id = ?
            """;

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS users(
                id SERIAL PRIMARY KEY,
                user_name VARCHAR(50) NOT NULL,
                first_name VARCHAR(50) NOT NULL,
                last_name VARCHAR(50) NOT NULL,
                passport int8 NOT NULL,
                email VARCHAR(50),
                bank_card VARCHAR(50) NOT NULL
            )
            """;

    @Override
    public void createTable() {
        // connection -> (transaction) ->
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties("db.url"),
                PropertiesUtil.getProperties("db.username"),
                PropertiesUtil.getProperties("db.password"));
             final var preparedStatement = connection.prepareStatement(CREATE_TABLE)) {
            // init ?
            preparedStatement.execute();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User get(String id) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties("db.url"),
                PropertiesUtil.getProperties("db.username"),
                PropertiesUtil.getProperties("db.password"));
             final var preparedStatement = connection.prepareStatement(SELECT_USER)) {
            // init ?
            preparedStatement.setInt(1, 1);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return User.builder()
                        .firstName(resultSet.getString("first_name"))
                        .userName(resultSet.getString("user_name"))
                        .lastName(resultSet.getString("last_name"))
                        .passport(resultSet.getInt("passport"))
                        .email(resultSet.getString("email"))
                        .bankCard(resultSet.getInt("bank_card"))
                        .build();
            } else {
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User update(String id, User obj) {
        return null;
    }

    @Override
    public User save(User obj) {
        return null;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<User> filterBy(Predicate<User> predicate) {
        return List.of();
    }
}
