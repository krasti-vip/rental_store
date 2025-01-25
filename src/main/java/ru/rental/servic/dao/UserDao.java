package ru.rental.servic.dao;

import ru.rental.servic.model.User;
import ru.rental.servic.util.PropertiesUtil;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class UserDao implements DAO<User, Integer> {

    private static final String SELECT_USER = """
            SELECT id, user_name, first_name, last_name, passport, email, bank_card FROM users WHERE id = ?
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

    private static final String UPDATE_USER = """
            UPDATE users 
            SET
                user_name = ?,
                first_name = ?,
                last_name = ?,
                passport = ?,
                email = ?,
                bank_card = ?
            WHERE id = ?
            """;

    private static final String INSERT_USER = """
            INSERT INTO users (user_name, first_name, last_name, passport, email, bank_card)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String DELETE_USER =
            "DELETE FROM users WHERE id = ?";

    private static final String SELECT_ALL_USERS =
            "SELECT id, user_name, first_name, last_name, passport, email, bank_card FROM users";

    private static final String BD_URL = "db.url";
    private static final String BD_USERNAME = "db.username";
    private static final String BD_PASSWORD = "db.password";

    public static boolean checkIfTableExists(String tableName) {
        String query = """
                SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = ?
                )
                """;

        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, tableName.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка существования таблицы", e);
        }

        return false;
    }

    @Override
    public void createTable() {

        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(CREATE_TABLE)) {

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка создания таблицы", e);
        }
    }

    @Override
    public User get(Integer id) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(SELECT_USER)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return User.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("first_name"))
                        .userName(resultSet.getString("user_name"))
                        .lastName(resultSet.getString("last_name"))
                        .passport(resultSet.getInt("passport"))
                        .email(resultSet.getString("email"))
                        .bankCard(resultSet.getLong("bank_card"))
                        .build();
            } else {
                return null;
            }


        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи пользователя", e);
        }
    }

    @Override
    public User update(Integer id, User obj) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(UPDATE_USER)) {

            preparedStatement.setString(1, obj.getUserName());
            preparedStatement.setString(2, obj.getFirstName());
            preparedStatement.setString(3, obj.getLastName());
            preparedStatement.setInt(4, obj.getPassport());
            preparedStatement.setString(5, obj.getEmail());
            preparedStatement.setLong(6, obj.getBankCard());
            preparedStatement.setInt(7, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return obj;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления пользователя", e);
        }
    }

    @Override
    public User save(User obj) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(INSERT_USER, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, obj.getUserName());
            preparedStatement.setString(2, obj.getFirstName());
            preparedStatement.setString(3, obj.getLastName());
            preparedStatement.setInt(4, obj.getPassport());
            preparedStatement.setString(5, obj.getEmail());
            preparedStatement.setLong(6, obj.getBankCard());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {

                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);

                        obj.setId(generatedId);
                        return obj;
                    }
                }
            }

            return null;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка сохранения", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(DELETE_USER)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка удаления", e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();

        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(SELECT_ALL_USERS);
             final var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getInt("id"))
                        .userName(resultSet.getString("user_name"))
                        .firstName(resultSet.getString("first_name"))
                        .lastName(resultSet.getString("last_name"))
                        .passport(resultSet.getInt("passport"))
                        .email(resultSet.getString("email"))
                        .bankCard(resultSet.getLong("bank_card"))
                        .build();
                users.add(user);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи всех пользователей", e);
        }

        return users;
    }

//    public void deleteAllUsers() {
//        try (final var connection = DriverManager.getConnection(
//                PropertiesUtil.getProperties(BD_URL),
//                PropertiesUtil.getProperties(BD_USERNAME),
//                PropertiesUtil.getProperties(BD_PASSWORD));
//             final var preparedStatement = connection.prepareStatement("DELETE FROM users")) {
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new IllegalStateException("Ошибка удаления всех юзеров", e);
//        }
//    }

    @Override
    public List<User> filterBy(Predicate<User> predicate) {
        List<User> allUsers = getAllUsers();
        return allUsers.stream()
                .filter(predicate)
                .toList();
    }
}
