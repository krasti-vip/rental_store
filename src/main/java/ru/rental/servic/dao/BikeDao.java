package ru.rental.servic.dao;

import org.springframework.stereotype.Component;
import ru.rental.servic.model.Bike;
import ru.rental.servic.util.ConnectionManager;
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
    public Bike get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID Bike не может быть null");
        }
        try (final var connection = ConnectionManager.getConnection();
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
    public Bike update(Integer id, Bike obj) {
        try (final var connection = ConnectionManager.getConnection();
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
                        return obj;
                    }
                }
            }

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

            return rowsAffected > 0;

        } catch (SQLException e) {
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
