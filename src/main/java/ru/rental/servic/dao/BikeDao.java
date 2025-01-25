package ru.rental.servic.dao;


import org.springframework.stereotype.Component;
import ru.rental.servic.model.Bike;
import ru.rental.servic.util.PropertiesUtil;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class BikeDao implements DAO<Bike, Integer> {

    private static final String SELECT_BIKE = """
            SELECT id, name, price, horse_power, volume FROM bikes WHERE id = ?
            """;

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS bikes(
                id SERIAL PRIMARY KEY,
                name VARCHAR(50) NOT NULL,
                price DOUBLE PRECISION NOT NULL,
                horse_power INT NOT NULL,
                volume DOUBLE PRECISION NOT NULL
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
            SELECT id, name, price, horse_power, volume FROM bikes
            """;

    private static final String BD_URL = "db.url";
    private static final String BD_USERNAME = "db.username";
    private static final String BD_PASSWORD = "db.password";

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
    public Bike get(Integer id) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(SELECT_BIKE)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return Bike.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .price(resultSet.getDouble("price"))
                        .horsePower(resultSet.getInt("horse_power"))
                        .volume(resultSet.getDouble("volume"))
                        .build();
            } else {
                return null;
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи байка", e);
        }
    }

    @Override
    public Bike update(Integer id, Bike obj) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(UPDATE_BIKE)) {

            preparedStatement.setString(1, obj.getName());
            preparedStatement.setDouble(2, obj.getPrice());
            preparedStatement.setInt(3, obj.getHorsePower());
            preparedStatement.setDouble(4, obj.getVolume());
            preparedStatement.setInt(5, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return obj;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления байка", e);
        }
    }

    @Override
    public Bike save(Bike obj) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
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
                        return obj;
                    }
                }
            }

            return null;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка сохранения байка", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(DELETE_BIKE)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка удаления байка", e);
        }
    }

    @Override
    public List<Bike> filterBy(Predicate<Bike> predicate) {
        List<Bike> allBikes = getAllBikes();
        return allBikes.stream()
                .filter(predicate)
                .toList();
    }

//    public void deleteAllBikes() {
//        try (final var connection = DriverManager.getConnection(
//                PropertiesUtil.getProperties(BD_URL),
//                PropertiesUtil.getProperties(BD_USERNAME),
//                PropertiesUtil.getProperties(BD_PASSWORD));
//             final var preparedStatement = connection.prepareStatement("DELETE FROM bikes")) {
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new IllegalStateException("Ошибка удаления всех мотиков", e);
//        }
//    }

    public List<Bike> getAllBikes() {
        List<Bike> bikes = new ArrayList<>();

        try (final var connection = DriverManager.getConnection(
                PropertiesUtil.getProperties(BD_URL),
                PropertiesUtil.getProperties(BD_USERNAME),
                PropertiesUtil.getProperties(BD_PASSWORD));
             final var preparedStatement = connection.prepareStatement(SELECT_ALL_BIKES);
             final var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Bike bike = Bike.builder()
                        .id(resultSet.getInt("id"))
                        .name(resultSet.getString("name"))
                        .price(resultSet.getDouble("price"))
                        .horsePower(resultSet.getInt("horse_power"))
                        .volume(resultSet.getDouble("volume"))
                        .build();
                bikes.add(bike);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи всех байков", e);
        }

        return bikes;
    }
}
