package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.CarDao;
import ru.rental.servic.dto.CarDto;
import ru.rental.servic.service.CarService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CarServiceTest extends BaseBd {

    private final CarService carService = new CarService(new CarDao());

    @Test
    @DisplayName("Test get")
    void getTest() {
        Integer carId = carService.getAll().get(3).getId();
        Optional<CarDto> carDto = carService.get(carId);
        assertTrue(carDto.isPresent(), "car с carId должен существовать");
        assertEquals("BMW", carService.get(carId).get().getTitle());
        assertThrows(IllegalArgumentException.class, () -> {
            carService.get(null);
        });
    }

    @Test
    @DisplayName("Test update")
    void updateTest() {
        Integer carId = carService.getAll().get(3).getId();
        CarDto carDto = CarDto.builder()
                .title("BMW")
                .price(540.5)
                .horsePower(450)
                .volume(5)
                .color("blue")
                .build();
        carService.update(carId, carDto);
        assertEquals(540.5, carService.get(carId).get().getPrice());
    }

    @Test
    @DisplayName("Test save and delete")
    void saveAndDeleteTest() {
        CarDto carDto = CarDto.builder()
                .title("Renault")
                .price(120)
                .horsePower(75)
                .volume(1.5)
                .color("white")
                .build();

        int carDtoId = carService.save(carDto).getId();
        assertEquals("Renault", carService.get(carDtoId).get().getTitle());
        assertEquals(6, carService.getAll().size());

        Integer carId = carService.getAll().get(4).getId();
        assertTrue(carService.get(carId).isPresent());
        assertEquals(6, carService.getAll().size());
        carService.delete(carId);
        Optional<CarDto> car = carService.get(carId);
        assertTrue(car.isEmpty());
        assertEquals(5, carService.getAll().size());
    }

    @Test
    @DisplayName("Test getAllBike")
    void getAllTest() {
        List<CarDto> cars = carService.getAll();
        assertTrue(cars.size() > 4);
    }

    @Test
    @DisplayName("Test filter")
    void filterTest() {
        List<CarDto> carFilter = carService.filterBy(u -> u.getTitle().equals("HYUNDAI"));
        assertTrue(carFilter.size() > 0);
        assertEquals("HYUNDAI", carFilter.get(0).getTitle());
    }
}
