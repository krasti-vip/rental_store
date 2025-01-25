package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dto.UserDto;
import ru.rental.servic.service.UserService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserServiceTest {

    private UserService userService;

    private int id;

    private UserDto userTest;

    @BeforeEach
    void setUp() {
        userService = new UserService();
        userTest = UserDto.builder()
                .userName("Jac")
                .firstName("Boyko")
                .lastName("Vasya")
                .passport(72698521)
                .bankCard(1234_4569_9874_1236L)
                .build();
        UserDto savedUser2 = userService.save(userTest);
        id = savedUser2.getId();
    }

    @Test
    @DisplayName("Тест get")
    void getTest() {

        Optional<UserDto> user = userService.get(id);
        assertTrue(user.isPresent(), "Пользователь с id должен существовать");
        assertEquals("Vasya", userService.get(id).get().getLastName());

        System.out.println(user.get());
    }

    @Test
    @DisplayName("Тест обновления")
    void updateTest() {

        UserDto userTest2 = UserDto.builder()
                .userName("Jac")
                .firstName("Boy")
                .lastName("Vas")
                .passport(72621)
                .bankCard(9876_4569_9874_1236L)
                .build();
        userService.update(id, userTest2);
        assertEquals("Vas", userService.get(id).get().getLastName());
    }

    @Test
    @DisplayName("Тест удаления")
    void deleteTest() {
        assertTrue(userService.get(id).isPresent());
        userService.delete(id);
        Optional<UserDto> user = userService.get(id);
        assertTrue(user.isEmpty());
    }

    @Test
    @DisplayName("Тест передачи всех users")
    void getAllTest() {
        List<UserDto> users = userService.getAll();
        assertTrue(users.size() > 0);
    }

    @Test
    @DisplayName("Тест фильтрации")
    void filterTest() {
        List<UserDto> usersFilter = userService.filterBy(u -> u.getUserName().equals("Jac"));
        assertTrue(usersFilter.size() > 0);
    }
}
