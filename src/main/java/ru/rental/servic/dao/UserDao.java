package ru.rental.servic.dao;

import org.springframework.stereotype.Component;
import ru.rental.servic.model.User;
import ru.rental.servic.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
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

    /**
     * Метод проверяет по переданному названию таблицы, ее существование, вернет или True, или False
     *
     * @param tableName
     * @return
     */
    @Override
    public boolean checkIfTableExists(String tableName) {
        String query = """
                SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = ?
                )
                """;

        try (final var connection = ConnectionManager.getConnection();
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

    /**
     * Метод создания таблицы в базе данных,
     * проверка на уникальность таблицы отсутствует, возможно дублирование, если по какой-то причине таблица не будет
     * создана выкинет Exception IllegalStateException("Ошибка создания таблицы", e);
     */
    @Override
    public void createTable() {

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(CREATE_TABLE)) {

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка создания таблицы", e);
        }
    }

    /**
     * Метод возвращает объект по его id, присутствует проверка на null переданного id, в этом
     * случае бросит ошибку IllegalArgumentException("ID obj не может быть null"), если id существует, но
     * метод не смог вернуть его то бросит ошибку IllegalStateException("Ошибка передачи obj", e);
     *
     * @param id
     * @return
     */
    @Override
    public User get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        try (final var connection = ConnectionManager.getConnection();
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

    /**
     * Метод обновляет объект по переданному id и новому объекту для обновления, отсутствует проверка
     * на null (могут быть проблемы), если обновление по другим причинам не произошло,
     * бросит exception IllegalStateException("Ошибка обновления obj", e);
     *
     * @param id
     * @param obj
     * @return
     */
    @Override
    public User update(Integer id, User obj) {
        try (final var connection = ConnectionManager.getConnection();
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

    /**
     * Метод сохраняет новый переданный объект, отсутствует проверка на null (осторожней),
     * если по другой причине не сохранится obj, кинет исключение IllegalStateException("Ошибка сохранения obj", e);
     *
     * @param obj
     * @return
     */
    @Override
    public User save(User obj) {
        try (final var connection = ConnectionManager.getConnection();
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

    /**
     * Метод удаляет объект по переданному id, отсутствует проверка на null (осторожней),
     * в другом случае если удаление не удалось, кинет исключение IllegalStateException("Ошибка удаления obj", e);
     *
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(DELETE_USER)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка удаления", e);
        }
    }

    /**
     * Метод возвращающий все объекты класса которые хранятся в базе данных, если передача не удалась,
     * кинет ошибку IllegalStateException("Ошибка передачи всех obj", e);
     *
     * @return
     */
    public List<User> getAll() {
        List<User> users = new ArrayList<>();

        try (final var connection = ConnectionManager.getConnection();
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

    /**
     * Метод осуществляет фильтрацию всех объектов находящихся в базе данных по переданному предикату и возвращает лист
     * с объектами удовлетворяющими критерии фильтрации, отсутствует проверка на null
     *
     * @param predicate
     * @return
     */
    @Override
    public List<User> filterBy(Predicate<User> predicate) {
        List<User> allUsers = getAll();
        return allUsers.stream()
                .filter(predicate)
                .toList();
    }
}
