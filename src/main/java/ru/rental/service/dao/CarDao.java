package ru.rental.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.model.Car;
import ru.rental.service.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class CarDao implements DAO<Car, Integer> {

    private static final String ID = "id";

    private static final String TITLE = "title";

    private static final String PRICE = "price";

    private static final String HORSE_POWER = "horse_power";

    private static final String VOLUME = "volume";

    private static final String COLOR = "color";

    private static final String USER_ID = "user_id";

    private final UserDao userDao;

    private static final Logger log = LoggerFactory.getLogger(CarDao.class);

    private static final String CAR_UPDATE = "Car with id {} has been updated!";

    @Autowired
    public CarDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private static final String SELECT_CAR = """
            SELECT id, title, price, horse_power, volume, color, user_id FROM cars WHERE id = ?
            """;

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS cars(
                id SERIAL PRIMARY KEY,
                title VARCHAR(50) NOT NULL,
                price DOUBLE PRECISION NOT NULL,
                horse_power INT NOT NULL,
                volume DOUBLE PRECISION NOT NULL,
                color VARCHAR(50) NOT NULL,
                userId INT REFERENCES users(id) ON DELETE RESTRICT
            )
            """;

    private static final String UPDATE_CAR = """
            UPDATE cars 
            SET
                title = ?,
                price = ?,
                horse_power = ?,
                volume = ?,
                color = ?
            WHERE id = ?
            """;

    private static final String INSERT_CAR = """
            INSERT INTO cars (title, price, horse_power, volume, color)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String DELETE_CAR = """
            DELETE FROM cars WHERE id = ?
            """;

    private static final String SELECT_ALL_CARS = """
            SELECT id, title, price, horse_power, volume, color, user_id FROM cars
            """;

    private static final String UPDATE_CAR_USER = """
                UPDATE cars
                SET user_id = ?
                WHERE id = ?
            """;

    private static final String SELECT_CARS_BY_USER_ID = """
            SELECT id, title, price, horse_power, volume, color, user_id FROM cars 
            WHERE user_id = ?
            """;

    private static final String CHECK_CARE_ID = """
            SELECT id FROM cars WHERE id = ?
            """;

    private static final String CHECK_TABLE = """
            SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = ?
            )
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
        log.info("Table {} does not exist!", tableName);
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
    public Car get(Integer id) {
        if (id == null) {
            log.warn("id is null");
            throw new IllegalArgumentException("ID Car не может быть null");
        }
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_CAR)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                log.info("Car ID {} exists!", id);
                return Car.builder()
                        .id(resultSet.getInt(ID))
                        .title(resultSet.getString(TITLE))
                        .price(resultSet.getDouble(PRICE))
                        .horsePower(resultSet.getInt(HORSE_POWER))
                        .volume(resultSet.getDouble(VOLUME))
                        .color(resultSet.getString(COLOR))
                        .userId(resultSet.getInt(USER_ID))
                        .build();
            } else {
                log.info("Car with id {} not found!", id);
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи авто", e);
        }
    }

    /**
     * Метод обновляет объект по переданному id и новому объекту для обновления, есть проверка
     * на null (через SQL запрос проверяем существует ли такой id если да, идем дальше если нет бросаем исключение),
     * если обновление по другим причинам не произошло, бросит exception IllegalStateException("Ошибка обновления obj", e);
     *
     * @param id
     * @param obj
     * @return
     */
    @Override
    public Car update(Integer id, Car obj) {
        try (final var connection = ConnectionManager.getConnection()) {

            try (final var checkStmt = connection.prepareStatement(CHECK_CARE_ID)) {
                checkStmt.setInt(1, id);
                try (final var rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Car with id " + id + " does not exist");
                    }
                }
            }
            try (final var preparedStatement = connection.prepareStatement(UPDATE_CAR)) {

                preparedStatement.setString(1, obj.getTitle());
                preparedStatement.setDouble(2, obj.getPrice());
                preparedStatement.setInt(3, obj.getHorsePower());
                preparedStatement.setDouble(4, obj.getVolume());
                preparedStatement.setString(5, obj.getColor());
                preparedStatement.setInt(6, id);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    log.info(CAR_UPDATE, id);
                    return obj;
                } else {
                    log.info("Car with id {} has not been updated!", id);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления авто", e);
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
    public Car save(Car obj) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(INSERT_CAR, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, obj.getTitle());
            preparedStatement.setDouble(2, obj.getPrice());
            preparedStatement.setInt(3, obj.getHorsePower());
            preparedStatement.setDouble(4, obj.getVolume());
            preparedStatement.setString(5, obj.getColor());


            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        obj.setId(generatedId);
                        log.info("Car with id {} has been saved!", generatedId);
                        return obj;
                    }
                }
            }
            log.info("Car with id {} has not been saved!", obj.getId());
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
             final var preparedStatement = connection.prepareStatement(DELETE_CAR)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            log.info("Car with id {} has been deleted!", id);
            return rowsAffected > 0;

        } catch (SQLException e) {
            log.warn("Car with id {} has not been deleted!", id);
            throw new IllegalStateException("Ошибка удаления таблицы", e);
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
    public List<Car> filterBy(Predicate<Car> predicate) {
        List<Car> allCars = getAll();
        log.info("filterBy");
        return allCars.stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Метод возвращающий все объекты класса которые хранятся в базе данных, если передача не удалась,
     * кинет ошибку IllegalStateException("Ошибка передачи всех obj", e);
     *
     * @return
     */
    public List<Car> getAll() {
        List<Car> cars = new ArrayList<>();

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_ALL_CARS);
             final var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Car car = Car.builder()
                        .id(resultSet.getInt(ID))
                        .title(resultSet.getString(TITLE))
                        .price(resultSet.getDouble(PRICE))
                        .horsePower(resultSet.getInt(HORSE_POWER))
                        .volume(resultSet.getDouble(VOLUME))
                        .color(resultSet.getString(COLOR))
                        .build();
                cars.add(car);
            }

        } catch (SQLException e) {
            log.warn("Car with id {} has not been loaded!", ID);
            throw new IllegalStateException("Ошибка передачи всех авто", e);
        }
        log.info("getALL");
        return cars;
    }

    public Car updateUserId(Integer carId, Integer userId) {
        try (final var connection = ConnectionManager.getConnection()) {

            try (final var checkStmt = connection.prepareStatement(CHECK_CARE_ID)) {
                checkStmt.setInt(1, carId);
                try (final var rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("Car with id " + carId + " does not exist");
                    }
                }
            }

            try (final var preparedStatement = connection.prepareStatement(UPDATE_CAR_USER)) {
                if (userId != null) {
                    preparedStatement.setInt(1, userId);
                } else {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                }
                preparedStatement.setInt(2, carId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {

                    Car updatedCar = get(carId);

                    if (userId != null) {
                        var maybeUser = userDao.get(userId);
                        if (maybeUser != null) {
                            maybeUser.getListCar().add(updatedCar);
                        }
                    }
                    log.info(CAR_UPDATE, carId);
                    return updatedCar;
                }
                log.warn("Car with id {} has not been updated!", carId);
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления userId машины", e);
        }
    }

    public List<Car> getAllByUserId(int userId) {
        List<Car> cars = new ArrayList<>();
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_CARS_BY_USER_ID)) {

            preparedStatement.setInt(1, userId);
            final var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                cars.add(Car.builder()
                        .id(resultSet.getInt(ID))
                        .title(resultSet.getString(TITLE))
                        .price(resultSet.getDouble(PRICE))
                        .horsePower(resultSet.getInt(HORSE_POWER))
                        .volume(resultSet.getDouble(VOLUME))
                        .color(resultSet.getString(COLOR))
                        .userId(resultSet.getInt(USER_ID))
                        .build());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка получения списка авто пользователя", e);
        }
        return cars;
    }
}
