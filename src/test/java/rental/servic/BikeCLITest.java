package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import ru.rental.service.crudApp.BikeCLI;
import ru.rental.service.dao.BikeDao;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.service.BikeService;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BikeCLITest {

    @Mock
    private BikeDao bikeDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private BikeService bikeService;

    @Mock
    private Scanner scanner;

    @InjectMocks
    private BikeCLI bikeCLI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetNewEntityInfo() {
        when(scanner.nextLine())
                .thenReturn("Harley Davidson")
                .thenReturn("15000.0")
                .thenReturn("1200.0")
                .thenReturn("80");

        BikeDto newBike = bikeCLI.getNewEntityInfo();

        assertEquals("Harley Davidson", newBike.getName());
        assertEquals(15000.0, newBike.getPrice());
        assertEquals(1200.0, newBike.getVolume());
        assertEquals(80, newBike.getHorsePower());
    }

    @Test
    void testGetUpdatedEntityInfo() {
        BikeDto bikeToUpdate = new BikeDto(1, "Old Bike", 10000.0, 60, 1000.0, null);

        when(scanner.nextLine())
                .thenReturn("Updated Bike")
                .thenReturn("20000")
                .thenReturn("1500.0")
                .thenReturn("100");

        BikeDto updatedBike = bikeCLI.getUpdatedEntityInfo(bikeToUpdate);

        assertEquals(1, updatedBike.getId());
        assertEquals("Updated Bike", updatedBike.getName());
        assertEquals(20000, updatedBike.getPrice());
        assertEquals(1500.0, updatedBike.getVolume());
        assertEquals(100, updatedBike.getHorsePower());
    }
}
