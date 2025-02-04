package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.BikeDao;
import ru.rental.servic.dao.UserDao;
import ru.rental.servic.dto.BikeDto;
import ru.rental.servic.service.BikeService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class BikeServiceTest extends BaseBd {

    private final BikeService bikeService = new BikeService(new BikeDao(new UserDao()));

    @Test
    @DisplayName("Test get")
    void getTest() {
        Integer bikeId = bikeService.getAll().get(3).getId();
        Optional<BikeDto> bikeDto = bikeService.get(bikeId);
        assertTrue(bikeDto.isPresent(), "bike с bikeId должен существовать");
        assertEquals("URAL", bikeService.get(bikeId).get().getName());
        assertThrows(IllegalArgumentException.class, () -> {
            bikeService.get(null);
        });
    }

    @Test
    @DisplayName("Test update")
    void updateTest() {
        Integer bikeId = bikeService.getAll().get(3).getId();
        BikeDto bikeDto = BikeDto.builder()
                .name("URAL")
                .price(2000)
                .horsePower(200)
                .volume(0.75)
                .build();
        bikeService.update(bikeId, bikeDto);
        assertEquals(0.75, bikeService.get(bikeId).get().getVolume());
    }

    @Test
    @DisplayName("Test save and delete")
    void saveAndDeleteTest() {
        BikeDto bikeDto = BikeDto.builder()
                .name("Planet")
                .price(156)
                .horsePower(25)
                .volume(0.5)
                .build();

        int bikeDtoId = bikeService.save(bikeDto).getId();
        assertEquals("Planet", bikeService.get(bikeDtoId).get().getName());
        assertEquals(6, bikeService.getAll().size());

        Integer bikeId = bikeService.getAll().get(4).getId();
        assertTrue(bikeService.get(bikeId).isPresent());
        assertEquals(6, bikeService.getAll().size());
        bikeService.delete(bikeId);
        Optional<BikeDto> bike = bikeService.get(bikeId);
        assertTrue(bike.isEmpty());
        assertEquals(5, bikeService.getAll().size());
    }

    @Test
    @DisplayName("Test getAllBike")
    void getAllTest() {
        List<BikeDto> bikes = bikeService.getAll();
        assertTrue(bikes.size() > 4);
    }

    @Test
    @DisplayName("Test filter")
    void filterTest() {
        List<BikeDto> bikeFilter = bikeService.filterBy(u -> u.getName().equals("SUZUKI"));
        assertTrue(bikeFilter.size() > 0);
        assertEquals("SUZUKI", bikeFilter.get(0).getName());
    }

//    @Test
//    @DisplayName("Test getAllByUserId")
//    void getAllByUserIdTest() {
//        // Создаем тестовые данные
//        Integer userId = 2; // предполагаем, что у пользователя с id = 2 есть байки
//        List<BikeDto> bikes = bikeService.getAllByUserId(userId);
//
//        // Проверка, что байки для пользователя с таким id были получены
//        assertNotNull(bikes, "Список байков не должен быть null");
//        assertTrue(bikes.size() > 0, "Должно быть больше одного байка для пользователя с id " + userId);
//
//        // Тест с неверным id, для которого нет байков
//        Integer invalidUserId = 999;
//        List<BikeDto> emptyBikes = bikeService.getAllByUserId(invalidUserId);
//
//        // Проверка, что список пуст, если у пользователя нет байков
//        assertNotNull(emptyBikes, "Список байков не должен быть null");
//        assertTrue(emptyBikes.isEmpty(), "Список байков должен быть пустым для пользователя с id " + invalidUserId);
//    }
//
//    @Test
//    @DisplayName("Test updateUserId")
//    void updateUserIdTest() {
//        // Создаем тестовые данные
//        Integer bikeId = bikeService.getAll().get(0).getId();  // Получаем существующий байк
//        Integer userId = 3;  // id пользователя, с которым нужно связать байк
//
//        // Обновляем userId для байка
//        Optional<BikeDto> updatedBike = bikeService.updateUserId(bikeId, userId);
//
//        // Проверяем, что обновление прошло успешно
//        assertTrue(updatedBike.isPresent(), "Байк с id " + bikeId + " должен быть успешно обновлен с userId " + userId);
//        assertEquals(userId, updatedBike.get().getUserId(), "userId должен быть обновлен");
//
//        // Пробуем обновить несуществующий байк
//        Integer invalidBikeId = 9999;  // Несуществующий id байка
//        Optional<BikeDto> invalidUpdate = bikeService.updateUserId(invalidBikeId, userId);
//
//        // Проверяем, что обновление не произошло для несуществующего байка
//        assertFalse(invalidUpdate.isPresent(), "Байк с id " + invalidBikeId + " не существует, обновление должно вернуть пустой Optional");
//    }
}
