package ru.rental.servic.dao;

import ru.rental.servic.model.Car;
import ru.rental.servic.util.PropertiesUtil;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    private static final String BD_URL = "db.url";
    private static final String BD_USERNAME = "db.username";
    private static final String BD_PASSWORD = "db.password";

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
    public Car get(Integer id) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
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

    @Override
    public Car update(Integer id, Car obj) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
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

    @Override
    public Car save(Car obj) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
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

    @Override
    public boolean delete(Integer id) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(DELETE_CAR)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка удаления таблицы", e);
        }
    }

    @Override
    public List<Car> filterBy(Predicate<Car> predicate) {
        List<Car> allCars = getAllCars();
        return allCars.stream()
                .filter(predicate)
                .toList();
    }

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();

        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
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

//    public void deleteAllCars() {
//        try (final var connection = DriverManager.getConnection(
//                PropertiesUtil.getProperties(BD_URL),
//                PropertiesUtil.getProperties(BD_USERNAME),
//                PropertiesUtil.getProperties(BD_PASSWORD));
//             final var preparedStatement = connection.prepareStatement("DELETE FROM cars")) {
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new IllegalStateException("Ошибка удаления всех auto", e);
//        }
//    }

    public static boolean checkIfTableExistsCar(String tableName) {
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
}
