package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import ru.rental.service.dao.UserDao;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest extends BaseBd {

    private final UserService userService = new UserService(new UserDao(), new ModelMapper());

    @Test
    @DisplayName("Test get")
    void getTest() {
        Integer userId = userService.getAll().get(3).getId();
        Optional<UserDto> user = userService.get(userId);
        assertTrue(user.isPresent(), "Пользователь с id должен существовать");
        assertEquals("ozi", userService.get(userId).get().getUserName());
        assertThrows(IllegalArgumentException.class, () -> {
            userService.get(null);
        });
    }

    @Test
    @DisplayName("Test update")
    void updateTest() {
        Integer userId = userService.getAll().get(3).getId();
        UserDto userTest2 = UserDto.builder()
                .userName("Jac")
                .firstName("Boy")
                .lastName("Vas")
                .passport(72621)
                .bankCard(9876_4569_9874_1236L)
                .listBike(List.of())
                .listCar(List.of())
                .build();
        userService.update(userId, userTest2);
        assertEquals("Jac", userService.get(userId).get().getUserName());
    }

    @Test
    @DisplayName("Test save and delete")
    void saveAndDeleteTest() {
        UserDto userDto = UserDto.builder()
                .userName("vandervud")
                .lastName("Tom")
                .firstName("Hardi")
                .passport(85234789)
                .email("hardi@mail.ru")
                .bankCard(258963214785L)

                .build();

        int userDtoId = userService.save(userDto).getId();
        assertEquals("vandervud", userService.get(userDtoId).get().getUserName());
        assertEquals(6, userService.getAll().size());

        Integer userId = userService.getAll().get(4).getId();
        assertTrue(userService.get(userId).isPresent());
        assertEquals(6, userService.getAll().size());
        userService.delete(userId);
        Optional<UserDto> user = userService.get(userId);
        assertTrue(user.isEmpty());
        assertEquals(5, userService.getAll().size());
    }

    @Test
    @DisplayName("Test getAllUsers")
    void getAllTest() {
        List<UserDto> users = userService.getAll();
        assertTrue(users.size() > 0);
    }

    @Test
    @DisplayName("Test filter")
    void filterTest() {
        List<UserDto> usersFilter = userService.filterBy(u -> u.getUserName().equals("bill"));
        assertTrue(usersFilter.size() > 0);
        assertEquals("bill", usersFilter.get(0).getUserName());
    }
}
