package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.CarDao;
import ru.rental.servic.model.Car;
import java.util.List;

public class CarServicTest {

    CarDao carDao = new CarDao();
    List<Car> bd = carDao.getAll();

    @BeforeEach
    public void setUp() {
        bd = carDao.getAll();
    }

    @Test
    @DisplayName("Проверка геттера")
    public void getTest() {
        Assertions.assertEquals("Жигули", carDao.get(2).getName(), "должна быть Жигули");
        Assertions.assertNotEquals("Жигули", carDao.get(1).getName(), "должен быть Жигули");
    }

    @Test
    @DisplayName("Проверка обновления car по id")
    public void updateTest() {
        carDao.update(3, new Car(3, "Хрукати", 770, 78, 1.8, "Red"));
        Assertions.assertEquals("Хрукати", carDao.get(3).getName(), "Должен быть Хрукати");
    }

    @Test
    @DisplayName("Проверка создания нового car")
    public void saveTest() {
        carDao.save(new Car(11, "МарусяCar", 500, 85, 9.25, "Gold"));
        Assertions.assertEquals(11, bd.size(), "должно быть 11");
        Assertions.assertEquals("Порше", carDao.get(1).getName(), "должен быть Порше");
        Assertions.assertEquals("МарусяCar", carDao.get(11).getName(), "Должен быть МарусяCar");
    }

    @Test
    @DisplayName("Проверка удаления")
    public void deleteTest() {
        carDao.delete(8);
        Assertions.assertEquals(9, bd.size());
        Assertions.assertNull(carDao.get(8));
    }

    @Test
    @DisplayName("Проверка фильтра")
    public void filtrTest() {
        List<Car> bdCar = carDao.filterBy(car -> car.getVolume() > 2.7);
        Assertions.assertEquals(5, bdCar.size(), "должно быть 5 нормальных cars");
    }
}
