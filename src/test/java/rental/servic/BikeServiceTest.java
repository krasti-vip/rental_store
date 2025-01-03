package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.BikeDao;
import ru.rental.servic.model.Bike;

import java.util.ArrayList;
import java.util.List;

public class BikeServiceTest {

    BikeDao bikeDao = new BikeDao();
    List<Bike> bd = bikeDao.getAll();

    @BeforeEach
    public void setUp() {
        List<Bike> bd = new ArrayList<>();
    }

    @Test
    @DisplayName("Проверка геттера")
    public void getTest() {
        Assertions.assertEquals("Хонда", bikeDao.get(2).getName(), "должна быть хонда");
        Assertions.assertNotEquals("Хонда", bikeDao.get(1).getName(), "должен быть ямаха");
    }

    @Test
    @DisplayName("Проверка обновления байка по id")
    public void updateTest() {
        bikeDao.update(3, new Bike(3, "Хрукати", 770, 78, 1.000));
        Assertions.assertEquals("Хрукати", bikeDao.get(3).getName(), "Должен быть Хрукати");
    }

    @Test
    @DisplayName("Проверка создания нового байка")
    public void saveTest() {
        bikeDao.save(new Bike(11, "МарусяМот", 500, 85, 9.25));
        Assertions.assertEquals(11, bd.size(), "должно быть 11");
        Assertions.assertEquals("Ямаха", bikeDao.get(1).getName(), "должен быть ямаха"); //как сравнить весь байк а не конкретные поля
        Assertions.assertEquals("МарусяМот", bikeDao.get(11).getName(), "Должен быть МарусяМот");
    }

    @Test
    @DisplayName("Проверка удаления")
    public void deleteTest() {
        bikeDao.delete(8);
        Assertions.assertEquals(9, bd.size());
        Assertions.assertNull(bikeDao.get(8));
    }

    @Test
    @DisplayName("Проверка фильтра")
    public void filtrTest() {
        List<Bike> bdBike = bikeDao.filterBy(bike -> bike.getVolume() > 0.7);
        Assertions.assertEquals(7, bdBike.size(), "должно быть 7 нормальных мотов");
    }
}
