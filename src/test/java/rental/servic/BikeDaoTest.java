package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.BikeDao;
import ru.rental.servic.model.Bike;
import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class BikeDaoTest extends BaseBd {

    // SINGLETON - это обьект в единственном числе для всей программы
    // PROTOTYPE - повторное создание одного и того же обьекта когда он требуется
    // DI - внедрение обьектов в другие обьекты через конструктор или сет или рефлексию(внутрь приватного не финального поля)
    // Context - хранилище bean(java обьекты)

    @Test
    @DisplayName("Test getAllBike")
    void bikeDaoGetAll() {
        final var bikeDao = new BikeDao();
        final var allBikes = bikeDao.getAll();

        assertEquals(5, allBikes.size());
    }

    @Test
    @DisplayName("Test creat Table")
    void createTableTest() {
        BikeDao bikeDao = new BikeDao();
        bikeDao.createTable();
        boolean creatTrue = bikeDao.checkIfTableExistsCar("bikes");
        assertTrue(creatTrue);
        boolean noCreat = bikeDao.checkIfTableExistsCar("test");
        assertFalse(noCreat);
    }

    @Test
    @DisplayName("Test getBike")
    void getBikeTest() {
        BikeDao bikeDao = new BikeDao();
        int bikeId = bikeDao.getAll().get(3).getId();
        assertEquals(4, bikeId);
        assertEquals("URAL", bikeDao.get(4).getName());
        assertNull(bikeDao.get(8));
    }

    @Test
    @DisplayName("Test update bike")
    void updateBikerTest() {
        BikeDao bikeDao = new BikeDao();
        int bikeId = bikeDao.getAll().get(2).getId();
        Bike bike = Bike.builder()
                .name("YAMAHA")
                .price(235.50)
                .horsePower(1)
                .volume(1.000)
                .build();
        assertEquals("YAMAHA", bikeDao.get(bikeId).getName());
        bikeDao.update(bikeId, bike);
        assertEquals(235.50, bikeDao.get(bikeId).getPrice());
        assertNull(bikeDao.update(9, bike));
    }

    @Test
    @DisplayName("Test save and delete bike")
    void saveTest() {
        BikeDao bikeDao = new BikeDao();
        Bike bike = Bike.builder()
                .name("Harley Davidson")
                .price(235.50)
                .horsePower(35)
                .volume(0.750)
                .build();
        Bike newBike = bikeDao.save(bike);
        assertNotNull(newBike);
        assertEquals(6, bikeDao.getAll().size());
        assertEquals("Harley Davidson", bikeDao.getAll().get(5).getName());
        int idBike = bikeDao.getAll().get(5).getId();
        assertEquals(6, idBike);
        assertTrue(bikeDao.delete(idBike));
        assertFalse(bikeDao.delete(9));
        assertEquals(5, bikeDao.getAll().size());
    }

    @Test
    @DisplayName("Test filtrBike")
    void filtrBikeTest() {
        BikeDao bikeDao = new BikeDao();
        Predicate<Bike> predicate = c -> c.getName().contains("URAL");
        List<Bike> filteredBike = bikeDao.filterBy(predicate);
        assertEquals("URAL", filteredBike.get(0).getName(), "модель должна быть 'URAL'");
        filteredBike.forEach(System.out::println);
    }
}
