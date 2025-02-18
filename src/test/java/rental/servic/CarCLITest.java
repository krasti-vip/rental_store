package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rental.service.crudApp.CarCLI;
import ru.rental.service.dto.CarDto;
import ru.rental.service.service.CarService;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CarCLITest {

    @Mock
    private CarService carService;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private CarCLI carCLI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNewEntityInfo() {
        when(scanner.nextLine())
                .thenReturn("Tesla Model S")
                .thenReturn("80000.0")
                .thenReturn("2.5")
                .thenReturn("500")
                .thenReturn("Black");

        CarDto newCar = carCLI.getNewEntityInfo();

        assertEquals("Tesla Model S", newCar.getTitle());
        assertEquals(80000.0, newCar.getPrice());
        assertEquals(2.5, newCar.getVolume());
        assertEquals(500, newCar.getHorsePower());
        assertEquals("Black", newCar.getColor());
    }

    @Test
    void testGetUpdatedEntityInfo() {
        CarDto carToUpdate = new CarDto(1, "Old Car", 50000.0, 300, 2.0, "White", null);

        when(scanner.nextLine())
                .thenReturn("Tesla Model X")
                .thenReturn("90000.0")
                .thenReturn("3.0")
                .thenReturn("600")
                .thenReturn("Red");

        CarDto updatedCar = carCLI.getUpdatedEntityInfo(carToUpdate);

        assertEquals(1, updatedCar.getId());
        assertEquals("Tesla Model X", updatedCar.getTitle());
        assertEquals(90000.0, updatedCar.getPrice());
        assertEquals(3.0, updatedCar.getVolume());
        assertEquals(600, updatedCar.getHorsePower());
        assertEquals("Red", updatedCar.getColor());
    }
}
