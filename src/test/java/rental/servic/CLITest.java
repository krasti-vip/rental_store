package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rental.service.crudApp.UserCLI;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.UserService;

import java.util.Optional;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CLITest {

    @Mock
    private Scanner scanner;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserCLI userCLI;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCrud_Exit() {
        when(scanner.nextLine()).thenReturn("6");

        userCLI.crud();

        verify(scanner, times(1)).nextLine();
    }

    @Test
    void testViewById_Success() {
        when(scanner.nextLine()).thenReturn("1");
        when(userService.get(1)).thenReturn(Optional.of(new UserDto(1, "user1", "John", "Doe", 123456789, "john.doe@example.com", 1234567890123456L, null, null)));

        userCLI.viewById();

        verify(userService).get(1);
    }

    @Test
    void testViewById_InvalidInput() {
        when(scanner.nextLine()).thenReturn("invalid");

        userCLI.viewById();

        verify(userService, never()).get(anyInt());
    }

    @Test
    void testUpdate_Success() {
        when(scanner.nextLine()).thenReturn("1");
        when(userService.get(1)).thenReturn(Optional.of(new UserDto(1, "user1", "John",
                "Doe", 123456789, "john.doe@example.com", 1234567890123456L,
                null, null)));

        when(userService.update(eq(1), any(UserDto.class))).thenReturn(Optional.of(new UserDto(1,
                "updated_user", "John", "Doe", 123456789,
                "john.doe@example.com", 1234567890123456L, null, null)));

        userCLI.update();

        verify(userService).get(1);
        verify(userService).update(eq(1), any(UserDto.class));
    }

    @Test
    void testAddNew_Success() {
        when(scanner.nextLine())
                .thenReturn("new_user")
                .thenReturn("Jane")
                .thenReturn("Doe")
                .thenReturn("987654321")
                .thenReturn("jane.doe@example.com")
                .thenReturn("9876543210987654");

        when(userService.save(any(UserDto.class))).thenReturn(
                new UserDto(1, "new_user", "Jane", "Doe", 987654321,
                        "jane.doe@example.com", 9876543210987654L, null, null)
        );

        userCLI.addNew();

        verify(userService).save(any(UserDto.class));
    }

    @Test
    void testDelete_Success() {
        when(scanner.nextLine()).thenReturn("1");
        when(userService.delete(1)).thenReturn(true);

        userCLI.delete();

        verify(userService).delete(1);
    }

    @Test
    void testFilter() {
        userCLI.filter();

        verify(scanner, never()).nextLine();
    }

    @Test
    void testGetNewEntityInfo() {
        when(scanner.nextLine())
                .thenReturn("new_user")
                .thenReturn("Jane")
                .thenReturn("Doe")
                .thenReturn("987654321")
                .thenReturn("jane.doe@example.com")
                .thenReturn("9876543210987654");

        UserDto newUser = userCLI.getNewEntityInfo();

        assertEquals("new_user", newUser.getUserName());
        assertEquals("Jane", newUser.getFirstName());
        assertEquals("Doe", newUser.getLastName());
        assertEquals(987654321, newUser.getPassport());
        assertEquals("jane.doe@example.com", newUser.getEmail());
        assertEquals(9876543210987654L, newUser.getBankCard());
    }

    @Test
    void testGetUpdatedEntityInfo() {
        UserDto userToUpdate = new UserDto(1, "old_user", "John", "Doe", 123456789, "john.doe@example.com", 1234567890123456L, null, null);

        when(scanner.nextLine())
                .thenReturn("updated_user")
                .thenReturn("John")
                .thenReturn("Doe")
                .thenReturn("123456789")
                .thenReturn("john.doe@example.com")
                .thenReturn("1234567890123456");

        UserDto updatedUser = userCLI.getUpdatedEntityInfo(userToUpdate);

        assertEquals(1, updatedUser.getId());
        assertEquals("updated_user", updatedUser.getUserName());
        assertEquals("John", updatedUser.getFirstName());
        assertEquals("Doe", updatedUser.getLastName());
        assertEquals(123456789, updatedUser.getPassport());
        assertEquals("john.doe@example.com", updatedUser.getEmail());
        assertEquals(1234567890123456L, updatedUser.getBankCard());
    }
}
