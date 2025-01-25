package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.CarDao;
import ru.rental.servic.model.Car;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class CarDaoTest {

    @Test
    @DisplayName("Тестирования всего Car")
    void carTest() {
        CarDao carDao = new CarDao();
        carDao.createTable();
        boolean table = carDao.checkIfTableExistsCar("cars");
        assertTrue(table);

        Car car = Car.builder()
                .title("bmw")
                .price(1236.25)
                .horsePower(354)
                .volume(3.500)
                .color("Red")
                .build();

        Car savedCar = carDao.save(car);
        assertNotNull(savedCar);
        assertEquals("bmw", savedCar.getTitle());

        Integer savedCarId = savedCar.getId();
        System.out.println(savedCarId);
        System.out.println(savedCar);
        assertEquals(carDao.get(savedCarId), savedCar, "что то не так c тестом get");

        Car car2 = Car.builder()
                .title("bmw")
                .price(1036.25)
                .horsePower(354)
                .volume(3.500)
                .color("Red")
                .build();

        carDao.update(car.getId(), car2);
        assertEquals(1036.25, carDao.get(savedCarId).getPrice());

        assertNotNull(car, "car должен быть");
        carDao.delete(car.getId());
        assertNull(carDao.get(car.getId()), "car должен быть удален");

        List<Car> bd = carDao.getAllCars();
        bd.forEach(System.out::println);
        assertNotNull(bd);

//        carDao.deleteAllCars();
        List<Car> bd2 = carDao.getAllCars();
        assertFalse(bd2.isEmpty());

        Car car3 = Car.builder()
                .title("mersedes")
                .price(1306.5)
                .horsePower(332)
                .volume(3.400)
                .color("white")
                .build();

        Car car4 = Car.builder()
                .title("opel")
                .price(106.5)
                .horsePower(69)
                .volume(1.200)
                .color("gold")
                .build();

        carDao.save(car3);
        carDao.save(car4);

        Predicate<Car> predicate = c -> c.getColor().contains("gold");

        List<Car> filteredCar = carDao.filterBy(predicate);

//        assertEquals(1, filteredCar.size(), "Должна быть найдена только одна машина");
        assertEquals("gold", filteredCar.get(0).getColor(), "цвет должен быть 'gold'");

        filteredCar.forEach(System.out::println);
    }
}
