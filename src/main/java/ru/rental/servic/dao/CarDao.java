package ru.rental.servic.dao;

import org.springframework.stereotype.Component;
import ru.rental.servic.model.Car;
import ru.rental.servic.util.ConnectionManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class CarDao implements DAO<Car, Integer> {

    private static final String SELECT_CAR = """
            SELECT id, title, price, horse_power, volume, color FROM cars WHERE id = ?
            """;

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS cars(
                id SERIAL PRIMARY KEY,
                title VARCHAR(50) NOT NULL,
                price DOUBLE PRECISION NOT NULL,
                horse_power INT NOT NULL,
                volume DOUBLE PRECISION NOT NULL,
                color VARCHAR(50) NOT NULL
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
            SELECT id, title, price, horse_power, volume, color FROM cars
            """;

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
    public Car get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID Car не может быть null");
        }
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_CAR)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Car.builder()
                        .id(resultSet.getInt("id"))
                        .title(resultSet.getString("title"))
                        .price(resultSet.getDouble("price"))
                        .horsePower(resultSet.getInt("horse_power"))
                        .volume(resultSet.getDouble("volume"))
                        .color(resultSet.getString("color"))
                        .build();
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи авто", e);
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
    public Car update(Integer id, Car obj) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(UPDATE_CAR)) {

            preparedStatement.setString(1, obj.getTitle());
            preparedStatement.setDouble(2, obj.getPrice());
            preparedStatement.setInt(3, obj.getHorsePower());
            preparedStatement.setDouble(4, obj.getVolume());
            preparedStatement.setString(5, obj.getColor());
            preparedStatement.setInt(6, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return obj;
            } else {
                return null;
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
             final var preparedStatement = connection.prepareStatement(DELETE_CAR)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
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
                        .id(resultSet.getInt("id"))
                        .title(resultSet.getString("title"))
                        .price(resultSet.getDouble("price"))
                        .horsePower(resultSet.getInt("horse_power"))
                        .volume(resultSet.getDouble("volume"))
                        .color(resultSet.getString("color"))
                        .build();
                cars.add(car);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи всех авто", e);
        }

        return cars;
    }
}
