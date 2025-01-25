package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.BikeDao;
import ru.rental.servic.model.Bike;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class BikeDaoTest extends BaseTest {

    // SINGLETON - это обьект в единственном числе для всей программы
    // PROTOTYPE - повторное создание одного и того же обьекта когда он требуется
    // DI - внедрение обьектов в другие обьекты через конструктор или сет или рефлексию(внутрь приватного не финального поля)
    // Context - хранилище bean(java обьекты)


    @Test
    void bikeDaoGetA() {
        final var bikeDao = new BikeDao();
        final var allBikes = bikeDao.getAllBikes();

        assertEquals(allBikes.size(), 5);
    }


    @Test
    @DisplayName("Тестирования всего Bike")
    void bikeTest() {
        BikeDao bikeDao = new BikeDao();
        bikeDao.createTable();
        boolean table = bikeDao.checkIfTableExistsCar("bikes");
        assertTrue(table);

        Bike bike = Bike.builder()
                .name("honda")
                .price(136.25)
                .horsePower(54)
                .volume(0.750)
                .build();

        Bike savedBike = bikeDao.save(bike);
        assertNotNull(savedBike);
        assertEquals("honda", savedBike.getName());

        Integer savedBikeId = savedBike.getId();
        System.out.println(savedBikeId);
        System.out.println(savedBike);
        assertEquals(bikeDao.get(savedBikeId), savedBike, "что то не так c тестом get");

        Bike bike2 = Bike.builder()
                .name("honda")
                .price(106.25)
                .horsePower(54)
                .volume(0.750)
                .build();

        bikeDao.update(bike.getId(), bike2);
        assertEquals(106.25, bikeDao.get(savedBikeId).getPrice());

        assertNotNull(bike, "bike должен быть");
        bikeDao.delete(bike.getId());
        assertNull(bikeDao.get(bike.getId()), "bike должен быть удален");

        List<Bike> bd = bikeDao.getAllBikes();
        bd.forEach(System.out::println);
        assertNotNull(bd);

//        bikeDao.deleteAllBikes();
        List<Bike> bd2 = bikeDao.getAllBikes();
        assertFalse(bd2.isEmpty());

        Bike bike3 = Bike.builder()
                .name("bmw")
                .price(156.5)
                .horsePower(106)
                .volume(1.400)
                .build();

        Bike bike4 = Bike.builder()
                .name("Ducati")
                .price(296.5)
                .horsePower(159)
                .volume(1.500)
                .build();

        bikeDao.save(bike3);
        bikeDao.save(bike4);

        Predicate<Bike> predicate = c -> c.getName().contains("Ducati");

        List<Bike> filteredBike = bikeDao.filterBy(predicate);

//        assertEquals(1, filteredBike.size(), "Должен быть найден только один байк");
        assertEquals(296.5, filteredBike.get(0).getPrice(), "цена должна быть 296.5");

        filteredBike.forEach(System.out::println);
    }
}
