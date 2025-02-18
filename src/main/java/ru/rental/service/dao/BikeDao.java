package ru.rental.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.model.Bike;
import ru.rental.service.util.ConnectionManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


@Component
public class BikeDao implements DAO<Bike, Integer> {

    private static final String ID = "id";

    private static final String NAME = "name";

    private static final String PRICE = "price";

    private static final String HORSE_POWER = "horse_power";

    private static final String VOLUME = "volume";

    private static final String USER_ID = "user_id";

    private static final Logger log = LoggerFactory.getLogger(BikeDao.class);

    private final UserDao userDao;

    @Autowired
    public BikeDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private static final String SELECT_BIKE = """
            SELECT id, name, price, horse_power, volume, user_id FROM bikes WHERE id = ?
            """;

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS bikes(
                id SERIAL PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                price DOUBLE PRECISION NOT NULL,
                horse_power INT NOT NULL,
                volume DOUBLE PRECISION NOT NULL,
                user_id INT REFERENCES users(id) ON DELETE RESTRICT
            )
            """;

    private static final String UPDATE_BIKE = """
            UPDATE bikes 
            SET
                name = ?,
                price = ?,
                horse_power = ?,
                volume = ?
            WHERE id = ?
            """;

    private static final String INSERT_BIKE = """
            INSERT INTO bikes (name, price, horse_power, volume)
            VALUES (?, ?, ?, ?)
            """;

    private static final String DELETE_BIKE = """
            DELETE FROM bikes WHERE id = ?
            """;

    private static final String SELECT_ALL_BIKES = """
            SELECT id, name, price, horse_power, volume, user_id FROM bikes
            """;

    private static final String UPDATE_BIKE_USER = """
                UPDATE bikes
                SET user_id = ?
                WHERE id = ?
            """;

    private static final String SELECT_BIKE_BY_USER_ID = """
            SELECT id, name, price, horse_power, volume, user_id FROM bikes 
            WHERE user_id = ?
            """;

    private static final String CHECK_TABLE = """
            SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = ?
            )
            """;

    private static final String CHECK_BIKE_ID = """
            SELECT 1 FROM bikes WHERE id = ?
            """;

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
                log.info("Table {} exists!", tableName);
                return resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка существования таблицы", e);
        }
        log.info("Table {} no exists!", tableName);
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
    public Bike get(Integer id) {
        if (id == null) {
            log.info("id is null!");
            throw new IllegalArgumentException("ID Bike не может быть null");
        }

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_BIKE)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                log.info("Bike {} exists!", id);
                return Bike.builder()
                        .id(resultSet.getInt(ID))
                        .name(resultSet.getString(NAME))
                        .price(resultSet.getDouble(PRICE))
                        .horsePower(resultSet.getInt(HORSE_POWER))
                        .volume(resultSet.getDouble(VOLUME))
                        .userId(resultSet.getInt(USER_ID))
                        .build();
            } else {
                log.info("Bike with id {} not found!", id);
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи байка", e);
        }
    }

    /**
     * Метод обновляет объект по переданному id и новому объекту для обновления, есть проверка
     * на null (через SQL запрос проверяем существует ли такой id если да, идем дальше если нет бросаем исключение),
     * если обновление по другим причинам не произошло,
     * бросит exception IllegalStateException("Ошибка обновления obj", e);
     *
     * @param id
     * @param obj
     * @return
     */
    @Override
    public Bike update(Integer id, Bike obj) {
        try (final var connection = ConnectionManager.getConnection()) {
            try (final var checkStmt = connection.prepareStatement(CHECK_BIKE_ID)) {
                checkStmt.setInt(1, id);
                try (final var rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Bike with id " + id + " does not exist");
                    }
                }
            }
            try (final var preparedStatement = connection.prepareStatement(UPDATE_BIKE)) {

                preparedStatement.setString(1, obj.getName());
                preparedStatement.setDouble(2, obj.getPrice());
                preparedStatement.setInt(3, obj.getHorsePower());
                preparedStatement.setDouble(4, obj.getVolume());
                preparedStatement.setInt(5, id);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    log.info("Bike {} has been updated!", id);
                    return obj;
                } else {
                    log.info("Bike {} has not been updated!", id);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления байка", e);
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
    public Bike save(Bike obj) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(INSERT_BIKE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, obj.getName());
            preparedStatement.setDouble(2, obj.getPrice());
            preparedStatement.setInt(3, obj.getHorsePower());
            preparedStatement.setDouble(4, obj.getVolume());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        obj.setId(generatedId);
                        log.info("Bike {} has been saved!", obj.getId());
                        return obj;
                    }
                }
            }
            log.info("Bike {} has not been saved!", obj.getId());
            return null;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка сохранения байка", e);
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
             final var preparedStatement = connection.prepareStatement(DELETE_BIKE)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            log.info("Bike {} has been deleted!", id);
            return rowsAffected > 0;

        } catch (SQLException e) {
            log.warn("Bike {} has not been deleted!", id);
            throw new IllegalStateException("Ошибка удаления байка", e);
        }
    }

    /**
     * Метод осуществляет фильтрацию всех объектов находящихся в базе данных по переданному предикату и возвращает лист
     * с объектами удовлетворяющими критерии фильтрации, отсутствует проверка на null
     *
     * @param predicate
     * @return
     */
    @Override
    public List<Bike> filterBy(Predicate<Bike> predicate) {
        List<Bike> allBikes = getAll();
        log.info("filterBy");
        return allBikes.stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Метод возвращающий все объекты класса которые хранятся в базе данных, если передача не удалась,
     * кинет ошибку IllegalStateException("Ошибка передачи всех obj", e);
     *
     * @return
     */
    public List<Bike> getAll() {
        List<Bike> bikes = new ArrayList<>();

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_ALL_BIKES);
             final var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Bike bike = Bike.builder()
                        .id(resultSet.getInt(ID))
                        .name(resultSet.getString(NAME))
                        .price(resultSet.getDouble(PRICE))
                        .horsePower(resultSet.getInt(HORSE_POWER))
                        .volume(resultSet.getDouble(VOLUME))
                        .build();
                bikes.add(bike);
            }

        } catch (SQLException e) {
            log.warn("Ошибка передачи всех байков");
            throw new IllegalStateException("Ошибка передачи всех байков", e);
        }
        log.info("getAll");
        return bikes;
    }

    public Bike updateUserId(Integer bikeId, Integer userId) {
        try (final var connection = ConnectionManager.getConnection()) {
            try (final var checkStmt = connection.prepareStatement(CHECK_BIKE_ID)) {
                checkStmt.setInt(1, bikeId);
                try (final var rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Bike with id " + bikeId + " does not exist");
                    }
                }
            }

            try (final var preparedStatement = connection.prepareStatement(UPDATE_BIKE_USER)) {
                if (userId != null) {
                    preparedStatement.setInt(1, userId);
                } else {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                }
                preparedStatement.setInt(2, bikeId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Bike updatedBike = get(bikeId);

                    if (userId != null) {
                        var maybeUser = userDao.get(userId);
                        if (maybeUser != null) {
                            maybeUser.getListBike().add(updatedBike);
                        }
                    }
                    log.info("Bike {} has been updated!", bikeId);
                    return updatedBike;
                }
                log.warn("Bike {} has not been updated!", bikeId);
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления userId машины", e);
        }
    }

    public List<Bike> getAllByUserId(int userId) {
        List<Bike> bikes = new ArrayList<>();
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_BIKE_BY_USER_ID)) {

            preparedStatement.setInt(1, userId);
            final var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bikes.add(Bike.builder()
                        .id(resultSet.getInt(ID))
                        .name(resultSet.getString(NAME))
                        .price(resultSet.getDouble(PRICE))
                        .horsePower(resultSet.getInt(HORSE_POWER))
                        .volume(resultSet.getDouble(VOLUME))
                        .userId(resultSet.getInt(USER_ID))
                        .build());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка получения списка bikes пользователя", e);
        }
        return bikes;
    }
}
