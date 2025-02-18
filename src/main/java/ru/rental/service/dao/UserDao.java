package ru.rental.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.rental.service.model.Bike;
import ru.rental.service.model.Car;
import ru.rental.service.model.User;
import ru.rental.service.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class UserDao implements DAO<User, Integer> {

    private static final Logger log = LoggerFactory.getLogger(UserDao.class);

    private static final String NO_USER_FOUND = "User with id {} not found";

    private static final String USER_FOUND = "Users loaded: {}";

    private static final String SELECT_USER = """
            SELECT id, user_name, first_name, last_name, passport, email, bank_card 
            FROM users 
            WHERE id = ?
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

    private static final String CHECK_TABLE = """
            SELECT EXISTS (
                SELECT FROM information_schema.tables 
                WHERE table_name = ?
            )
            """;

    private static final String CHECK_USER_ID =
            "SELECT id FROM users WHERE id = ?";

    private static final String QUERY_BIKE =
            "SELECT id, name, price, horse_power, volume, user_id FROM bikes WHERE user_id = ?";

    private static final String QUERY_CAR =
            "SELECT id, title, price, horse_power, volume, color, user_id FROM cars WHERE user_id = ?";

    /**
     * Метод проверяет по переданному названию таблицы, ее существование, вернет или True, или False
     *
     * @param tableName
     * @return
     */
    @Override
    public boolean checkIfTableExists(String tableName) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(CHECK_TABLE)) {

            preparedStatement.setString(1, tableName.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                log.info("Table exists: {}", tableName);
                return resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка существования таблицы", e);
        }
        log.warn("Table no exists: {}", tableName);
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
            log.warn("id is null");
            throw new IllegalArgumentException("ID пользователя не может быть null");
        }
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_USER)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                log.info("User: найден");
                return User.builder()
                        .id(resultSet.getInt("id"))
                        .firstName(resultSet.getString("first_name"))
                        .userName(resultSet.getString("user_name"))
                        .lastName(resultSet.getString("last_name"))
                        .passport(resultSet.getInt("passport"))
                        .email(resultSet.getString("email"))
                        .bankCard(resultSet.getLong("bank_card"))
                        .listBike(getUserBikes(id))
                        .listCar(getUserCars(id))
                        .build();
            } else {
                log.warn(NO_USER_FOUND, id);
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
        try (final var connection = ConnectionManager.getConnection()) {

            try (final var checkStmt = connection.prepareStatement(CHECK_USER_ID)) {
                checkStmt.setInt(1, id);
                try (final var rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        log.warn(NO_USER_FOUND, id);
                        throw new IllegalStateException("Car with id " + id + " does not exist");
                    }
                }
            }
            try (final var preparedStatement = connection.prepareStatement(UPDATE_USER)) {

                preparedStatement.setString(1, obj.getUserName());
                preparedStatement.setString(2, obj.getFirstName());
                preparedStatement.setString(3, obj.getLastName());
                preparedStatement.setInt(4, obj.getPassport());
                preparedStatement.setString(5, obj.getEmail());
                preparedStatement.setLong(6, obj.getBankCard());
                preparedStatement.setInt(7, id);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    log.info("User with id {} updated", id);
                    return obj;
                } else {
                    log.warn(NO_USER_FOUND, id);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления пользователя", e);
        }
    }

    /**
     * Метод сохраняет новый переданный объект, добавлена проверка на null,
     * если по другой причине не сохранится obj, кинет исключение IllegalStateException("Ошибка сохранения obj", e);
     *
     * @param obj
     * @return
     */
    @Override
    public User save(User obj) {
        if (obj == null) {
            log.warn("User object is null");
            throw new IllegalArgumentException("Пользователь не может быть null");
        }
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
                        log.info("User with id {} save", obj.getId());
                        return obj;
                    }
                }
            }
            log.warn(NO_USER_FOUND, obj.getId());
            return null;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка сохранения", e);
        }
    }

    /**
     * Метод удаляет объект по переданному id, добавлена проверка на null,
     * в другом случае если удаление не удалось, кинет исключение IllegalStateException("Ошибка удаления obj", e);
     *
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        if (id == null) {
            log.warn("id is null");
            throw new IllegalArgumentException("Id не может быть null");
        }
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(DELETE_USER)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            log.info("User with id {} deleted", id);
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
                log.info("User with id {} loaded", user.getId());
                users.add(user);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи всех пользователей", e);
        }
        log.info(USER_FOUND, users);
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
        log.info(USER_FOUND, allUsers);
        return allUsers.stream()
                .filter(predicate)
                .toList();
    }

    private List<Bike> getUserBikes(int userId) {
        List<Bike> bikes = new ArrayList<>();

        try (var connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(QUERY_BIKE)) {

            preparedStatement.setInt(1, userId);
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bikes.add(Bike.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .price(resultSet.getDouble("price"))
                        .horsePower(resultSet.getInt("horse_power"))
                        .volume(resultSet.getDouble("volume"))
                        .userId(userId)
                        .build());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка при загрузке байков", e);
        }
        log.info(USER_FOUND, bikes);
        return bikes;
    }

    private List<Car> getUserCars(int userId) {
        List<Car> cars = new ArrayList<>();

        try (var connection = ConnectionManager.getConnection();
             var preparedStatement = connection.prepareStatement(QUERY_CAR)) {

            preparedStatement.setInt(1, userId);
            var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cars.add(Car.builder()
                        .id(resultSet.getInt("id"))
                        .title(resultSet.getString("title"))
                        .price(resultSet.getDouble("price"))
                        .horsePower(resultSet.getInt("horse_power"))
                        .volume(resultSet.getDouble("volume"))
                        .color(resultSet.getString("color"))
                        .userId(userId)
                        .build());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка при загрузке машин", e);
        }
        log.info(USER_FOUND, cars);
        return cars;
    }
}
