package rental.servic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.BikeDao;
import ru.rental.servic.model.Bike;

import java.util.List;
import java.util.function.Predicate;

public class BikeDaoTest {

    BikeDao bikeDao = new BikeDao();
    List<Bike> bd = bikeDao.getAll();

    @Test
    @DisplayName("Проверка геттера")
    public void getTest() {
        Assertions.assertEquals("Ямаха", bd.get(0).getName(), "Должна быть Ямаха");
        Assertions.assertEquals("Кавасаки", bd.get(4).getName(), "Должен быть кавасаки");
    }

    @Test
    @DisplayName("Проверка обновления")
    public void updateTest() {
        Bike newBike = new Bike(2,"Ямаха супер пупер", 5000, 800, 2.000);
        Bike result = bikeDao.update(2, newBike);
        Assertions.assertNotNull(result, "Байк должен быть");
        Assertions.assertEquals("Ямаха супер пупер", bd.get(1).getName(), "Должен буть " +
                "ямаха супер пупер");
    }

    @Test
    @DisplayName("Проверка сохранения нового байка")
    public void saveTest() {
        Bike newBike = new Bike(11,"Ямаха супер пупер", 5000, 800, 2.000); //можно ли сделать в одну строчку
        Bike result = bikeDao.save(newBike);
        Assertions.assertEquals("Ямаха супер пупер", bd.get(10).getName(), "Должен быть" +
                " ямаха супер пупер");
        Assertions.assertEquals(11,bd.size(), "Должно быть 11 байков");
    }
    
    @Test
    @DisplayName("Проверка удаления мотоцикла")
    public void deleteTest() {
        Assertions.assertEquals(10, bd.size(), "Изначально лист должен быть длиной 10");
        boolean deleteBike = bikeDao.delete(10);
        Assertions.assertTrue(deleteBike, "байк должен быть удален");
        Assertions.assertEquals(9, bd.size(), "Лист должен быть длиной 9");
    }

    @Test
    @DisplayName("Проверка фильтрации")
    public void filterBikeTest() {
        Predicate<Bike> filtrBike = bike -> bike.getName().startsWith("К");
        List<Bike> filteredBike = bikeDao.filterBy(filtrBike);
        Assertions.assertFalse(filteredBike.isEmpty());
        for(Bike bike : filteredBike) {
            Assertions.assertTrue(bike.getName().startsWith("К"));
        }
    }
}
