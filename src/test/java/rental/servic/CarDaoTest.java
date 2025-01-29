package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.CarDao;
import ru.rental.servic.model.Car;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class CarDaoTest extends BaseBd{

    CarDao carDao;

    @BeforeEach
    void setup() {
        carDao = new CarDao();
    }

    @Test
    @DisplayName("Test getAllCar")
    void carDaoGetAll() {
        final var allCars = carDao.getAll();

        assertEquals(5, allCars.size());
    }

    @Test
    @DisplayName("Test creat Table Car")
    void createTableTest() {
        carDao.createTable();
        boolean creatTrue = carDao.checkIfTableExists("cars");
        assertTrue(creatTrue);
        boolean noCreat = carDao.checkIfTableExists("test");
        assertFalse(noCreat);
    }

    @Test
    @DisplayName("Test getCar")
    void getCarTest() {
        int carId = carDao.getAll().get(3).getId();
        assertEquals(4, carId);
        assertEquals("BMW", carDao.get(4).getTitle());
        assertNull(carDao.get(8));
    }

    @Test
    @DisplayName("Test update car")
    void updateCarTest() {
        int carId = carDao.getAll().get(2).getId();
        Car car = Car.builder()
                .title("HYUNDAI")
                .price(320.9)
                .horsePower(156)
                .volume(2.2)
                .color("white")
                .build();
        assertEquals("HYUNDAI", carDao.get(carId).getTitle());
        carDao.update(carId, car);
        assertEquals(2.2, carDao.get(carId).getVolume());
        assertNull(carDao.update(9, car));
    }

    @Test
    @DisplayName("Test save and delete car")
    void saveAnaDeleteTest() {
        Car car = Car.builder()
                .title("mersedes")
                .price(1306.5)
                .horsePower(332)
                .volume(3.400)
                .color("white")
                .build();
        Car newCar = carDao.save(car);
        assertNotNull(newCar);
        assertEquals(6, carDao.getAll().size());
        assertEquals("mersedes", carDao.getAll().get(5).getTitle());
        int idCar = carDao.getAll().get(5).getId();
        assertEquals(6, idCar);
        assertTrue(carDao.delete(idCar));
        assertFalse(carDao.delete(9));
        assertEquals(5, carDao.getAll().size());
    }

    @Test
    @DisplayName("Test filtrCar")
    void filtrCarTest() {
        Predicate<Car> predicate = c -> c.getColor().contains("gold");
        List<Car> filteredCar = carDao.filterBy(predicate);
        assertEquals("gold", filteredCar.get(0).getColor(), "цвет должен быть 'gold'");
        filteredCar.forEach(System.out::println);
    }
}
