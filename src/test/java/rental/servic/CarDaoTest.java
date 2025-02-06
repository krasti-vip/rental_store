package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.rental.servic.dao.CarDao;
import ru.rental.servic.model.Car;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class CarDaoTest extends BaseBd {

    private static final CarDao CAR_DAO = new CarDao();

    private static Stream<Arguments> sourceCar() {
        return Stream.of(
                Arguments.of(
                        Car.builder()
                                .id(1)
                                .title("MERCEDES")
                                .price(655.3)
                                .horsePower(250)
                                .volume(3.5)
                                .color("black")
                                .build()
                ),
                Arguments.of(
                        Car.builder()
                                .id(2)
                                .title("HONDA")
                                .price(360.5)
                                .horsePower(190)
                                .volume(2.4)
                                .color("red")
                                .build()
                ),
                Arguments.of(
                        Car.builder()
                                .id(3)
                                .title("HYUNDAI")
                                .price(320.9)
                                .horsePower(156)
                                .volume(2)
                                .color("white")
                                .build()
                ),
                Arguments.of(
                        Car.builder()
                                .id(4)
                                .title("BMW")
                                .price(640.5)
                                .horsePower(450)
                                .volume(5)
                                .color("blue")
                                .build()
                ),
                Arguments.of(
                        Car.builder()
                                .id(5)
                                .title("OPEL")
                                .price(210.9)
                                .horsePower(110)
                                .volume(1.8)
                                .color("gold")
                                .build()
                )
        );
    }

    private static Stream<Arguments> sourceCarForFilterTest() {
        return Stream.of(
                Arguments.of(new Car(1, "MERCEDES", 655.3, 250, 3.5, "black"
                ), "MERCEDES", "MERCEDES"),
                Arguments.of(new Car(2, "HONDA", 360.5, 190, 2.4, "red"
                ), "HONDA", "HONDA"),
                Arguments.of(new Car(3, "HYUNDAI", 320.9, 156, 2, "white"
                ), "HYUNDAI", "HYUNDAI"),
                Arguments.of(new Car(4, "BMW", 640.5, 450, 5, "blue"
                ), "BMW", "BMW"),
                Arguments.of(new Car(5, "OPEL", 210.9, 110, 1.8, "gold"
                ), "OPEL", "OPEL")
        );
    }

    @Test
    @DisplayName("Test getAllCar")
    void carDaoGetAll() {
        final var allCars = CAR_DAO.getAll();
        assertEquals(5, allCars.size());
    }

    @Test
    @DisplayName("Test creat Table Cars")
    void createTableTest() {
        CAR_DAO.createTable();
        boolean creatTrue = CAR_DAO.checkIfTableExists("cars");
        assertTrue(creatTrue);
        boolean noCreat = CAR_DAO.checkIfTableExists("test");
        assertFalse(noCreat);
    }

    @ParameterizedTest
    @MethodSource("sourceCar")
    @DisplayName("Test getCar")
    void getBikeTest(Car sourceCar) {
        final var car = CAR_DAO.get(sourceCar.getId());

        assertAll(
                () -> assertEquals(car.getId(), sourceCar.getId()),
                () -> assertEquals(car.getTitle(), sourceCar.getTitle()),
                () -> assertEquals(car.getPrice(), sourceCar.getPrice()),
                () -> assertEquals(car.getVolume(), sourceCar.getVolume()),
                () -> assertEquals(car.getHorsePower(), sourceCar.getHorsePower()),
                () -> assertEquals(car.getColor(), sourceCar.getColor())
        );
    }

    @ParameterizedTest
    @DisplayName("Test update car")
    @MethodSource("sourceCar")
    void updateCarTest(Car sourceCar) {

        Car updatedCar = new Car(
                sourceCar.getId(),
                sourceCar.getTitle(),
                sourceCar.getPrice() + 1000,
                sourceCar.getHorsePower() + 20,
                sourceCar.getVolume(),
                sourceCar.getColor()
        );

        Car nonCar = new Car(
                -1,
                sourceCar.getTitle(),
                sourceCar.getPrice() + 1000,
                sourceCar.getHorsePower() + 20,
                sourceCar.getVolume(),
                sourceCar.getColor()
        );

        int nonCarId = nonCar.getId();

        final var updatedCarBd = CAR_DAO.update(sourceCar.getId(), updatedCar);
        final var carUpdate = CAR_DAO.get(updatedCarBd.getId());

        assertAll(
                () -> assertEquals(carUpdate.getId(), updatedCar.getId()),
                () -> assertEquals(carUpdate.getTitle(), updatedCar.getTitle()),
                () -> assertEquals(carUpdate.getPrice(), updatedCar.getPrice()),
                () -> assertEquals(carUpdate.getHorsePower(), updatedCar.getHorsePower()),
                () -> assertEquals(carUpdate.getVolume(), updatedCar.getVolume()),
                () -> assertEquals(carUpdate.getColor(), updatedCar.getColor()),
                () -> assertThrows(IllegalStateException.class, () -> {
                    CAR_DAO.update(nonCarId, updatedCar);
                }, "Expected update to throw an exception as the car does not exist")
        );
    }

    @ParameterizedTest
    @MethodSource("sourceCar")
    @DisplayName("Test save and delete car")
    void saveAndDeleteCarTest(Car sourceCar) {
        final var carDao = CAR_DAO;

        Car CarToSave = new Car(
                -896,
                sourceCar.getTitle(),
                sourceCar.getPrice(),
                sourceCar.getHorsePower(),
                sourceCar.getVolume(),
                sourceCar.getColor()
        );

        Car savedCar = carDao.save(CarToSave);
        assertNotNull(savedCar, "Car должен быть успешно сохранен");

        assertEquals(6, carDao.getAll().size(), "После сохранения в базе должно быть 6 байков");
        assertTrue(carDao.getAll().stream().anyMatch(b -> b.getId() == savedCar.getId()), "Сохраненный байк должен быть в списке");

        Car savedCarFromDb = carDao.get(savedCar.getId());
        assertAll(
                () -> assertEquals(CarToSave.getTitle(), savedCarFromDb.getTitle(), "Имя car должно совпадать"),
                () -> assertEquals(CarToSave.getPrice(), savedCarFromDb.getPrice(), "Цена car должна совпадать"),
                () -> assertEquals(CarToSave.getHorsePower(), savedCarFromDb.getHorsePower(), "Мощность car должна совпадать"),
                () -> assertEquals(CarToSave.getVolume(), savedCarFromDb.getVolume(), "Объем car должен совпадать"),
                () -> assertEquals(CarToSave.getColor(), savedCarFromDb.getColor(), "Цвет car должен совпадать")
        );

        assertTrue(carDao.delete(savedCar.getId()), "Car должен быть успешно удален");
        assertFalse(carDao.delete(999), "Попытка удаления несуществующего car должна вернуть false");
        assertEquals(5, carDao.getAll().size(), "После удаления car в базе должно быть 5 cars");
    }

    @ParameterizedTest
    @MethodSource("sourceCarForFilterTest")
    @DisplayName("Test filterBike")
    void filtrCarTest(Car sourceCarForFilterTest, String filterKeyword, String expectedName) {
        final var carDao = CAR_DAO;

        Predicate<Car> predicate = car -> car.getTitle().contains(filterKeyword);

        List<Car> filteredCars = carDao.filterBy(predicate);

        assertTrue(filteredCars.stream().anyMatch(b -> b.getTitle().equals(expectedName)),
                "Отфильтрованный car должен иметь модель " + expectedName);
    }
}
