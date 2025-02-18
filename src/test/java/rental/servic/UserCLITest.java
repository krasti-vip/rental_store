package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;
import ru.rental.service.crudApp.UserCLI;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.BikeService;
import ru.rental.service.service.CarService;
import ru.rental.service.service.UserService;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class UserCLITest {

    @Mock
    private UserService userService;

    @Mock
    private Scanner scanner;

    @Mock
    private ApplicationContext context;

    @Mock
    private CarService carService;

    @Mock
    private BikeService bikeService;

    @InjectMocks
    private UserCLI userCLI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getNewEntityInfoTest() {
        when(scanner.nextLine())
                .thenReturn("john_doe")
                .thenReturn("John")
                .thenReturn("Doe")
                .thenReturn("1234567890")
                .thenReturn("john.doe@example.com")
                .thenReturn("1234567890123456");

        UserDto newUser = userCLI.getNewEntityInfo();

        assertEquals("john_doe", newUser.getUserName());
        assertEquals("John", newUser.getFirstName());
        assertEquals("Doe", newUser.getLastName());
        assertEquals(1234567890, newUser.getPassport());
        assertEquals("john.doe@example.com", newUser.getEmail());
        assertEquals(1234567890123456L, newUser.getBankCard());
    }

    @Test
    void getUpdatedEntityInfoTest() {
        UserDto userToUpdate = new UserDto(1, "old_user", "Old", "User",
                987654321, "old.user@example.com", 9876543210987654L, null, null);

        when(scanner.nextLine())
                .thenReturn("new_user")
                .thenReturn("New")
                .thenReturn("User")
                .thenReturn("1234567890")
                .thenReturn("new.user@example.com")
                .thenReturn("1234567890123456");

        UserDto updatedUser = userCLI.getUpdatedEntityInfo(userToUpdate);

        assertEquals(1, updatedUser.getId());
        assertEquals("new_user", updatedUser.getUserName());
        assertEquals("New", updatedUser.getFirstName());
        assertEquals("User", updatedUser.getLastName());
        assertEquals(1234567890, updatedUser.getPassport());
        assertEquals("new.user@example.com", updatedUser.getEmail());
        assertEquals(1234567890123456L, updatedUser.getBankCard());
    }

    @Test
    void addCarAndBikeUser_AddCarTest() {
        when(context.getBean("carService", CarService.class)).thenReturn(carService);
        when(context.getBean("bikeService", BikeService.class)).thenReturn(bikeService);

        when(scanner.nextLine()).thenReturn("1");
        when(scanner.nextInt()).thenReturn(2);

        userCLI.addCarAndBikeUser();

        // Проверяем, что методы были вызваны
        //      verify(carService).updateUserId(eq(2), eq(2)); // Проверяем, что updateUserId вызван с правильными аргументами
        verify(bikeService, never()).updateUserId(anyInt(), anyInt());
    }

    @Test
    void addCarAndBikeUser_AddBikeTest() {
        when(context.getBean("carService", CarService.class)).thenReturn(carService);
        when(context.getBean("bikeService", BikeService.class)).thenReturn(bikeService);

        when(scanner.nextLine())
                .thenReturn("2")
                .thenReturn("");
        when(scanner.nextInt()).thenReturn(2);

        userCLI.addCarAndBikeUser();

        // Проверяем, что методы были вызваны
        //       verify(bikeService).updateUserId(eq(2), eq(2)); // Проверяем, что updateUserId вызван с правильными аргументами
        verify(carService, never()).updateUserId(anyInt(), anyInt());
    }

    @Test
    void addCarAndBikeUser_InvalidChoiceTest() {
        when(context.getBean("carService", CarService.class)).thenReturn(carService);
        when(context.getBean("bikeService", BikeService.class)).thenReturn(bikeService);

        when(scanner.nextLine()).thenReturn("3");
        when(scanner.nextInt()).thenReturn(789);

        userCLI.addCarAndBikeUser();

        verify(carService, never()).updateUserId(anyInt(), anyInt());
        verify(bikeService, never()).updateUserId(anyInt(), anyInt());
    }

    @Test
    void addCarAndBikeUser_CarNotFoundTest() {
        when(context.getBean("carService", CarService.class)).thenReturn(carService);
        when(context.getBean("bikeService", BikeService.class)).thenReturn(bikeService);

        when(scanner.nextLine()).thenReturn("1");
        when(scanner.nextInt()).thenReturn(6);

        doThrow(new IllegalStateException("Car with id 6 does not exist"))
                .when(carService).updateUserId(eq(6), eq(6));
    }
}