package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.rental.servic.dao.CarDao;
import ru.rental.servic.model.Car;

import java.util.List;
import java.util.function.Predicate;

public class CarDaoTest {

    CarDao carDao = new CarDao();
    List<Car> bd = carDao.getAll();

    @Test
    @DisplayName("Проверка геттера")
    public void getTest() {
        Assertions.assertEquals("Порше", bd.get(0).getName(), "Должна быть Порше");
        Assertions.assertEquals("Мерседес", bd.get(3).getName(), "Должен быть Мерседес");
    }

    @Test
    @DisplayName("Проверка обновления")
    public void updateTest() {
        Car updateCar = new Car(1,"Порше", 2000, 220, 2.5, "Черный");
        Car result = carDao.update(1, updateCar);
        Assertions.assertNotNull(result, "Авто должно быть");
        Assertions.assertEquals(2000, bd.get(0).getPrice(), "Цена должена быть " +
                2000);
    }

    @Test
    @DisplayName("Проверка сохранения нового авто")
    public void saveTest() {
        Car newCar = new Car(11,"Порше GT", 2000, 220, 2.5, "Черный"); //можно ли сделать в одну строчку
        Car result = carDao.save(newCar);
        Assertions.assertEquals("Порше GT", bd.get(10).getName(), "Должен быть" +
                "Порше GT");
        Assertions.assertEquals(11,bd.size(), "Должно быть 11 байков");
    }

    @Test
    @DisplayName("Проверка удаления авто")
    public void deleteTest() {
        Assertions.assertEquals(10, bd.size(), "Изначально лист должен быть длиной 10");
        boolean deleteCar = carDao.delete(10);
        Assertions.assertTrue(deleteCar, "Авто должено быть удалено");
        Assertions.assertEquals(9, bd.size(), "Лист должен быть длиной 9");
    }

    @Test
    @DisplayName("Проверка фильтрации")
    public void filterCarTest() {
        Predicate<Car> filtrCar = car -> car.getName().startsWith("П");
        List<Car> filteredCar = carDao.filterBy(filtrCar);
        Assertions.assertFalse(filteredCar.isEmpty());
        for(Car car : filteredCar) {
            Assertions.assertTrue(car.getName().startsWith("П"));
        }
    }
}
