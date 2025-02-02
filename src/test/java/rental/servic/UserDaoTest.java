package rental.servic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.UserDao;
import ru.rental.servic.model.User;
import java.util.List;
import java.util.function.Predicate;
import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest extends BaseBd {

    UserDao userDao;

    @BeforeEach
    void setup() {
        userDao = new UserDao();
    }

    @Test
    @DisplayName("Test 5 users in table, method getAllUsers")
    void userDaoGetAll() {
        final var allUser = userDao.getAll();
        assertEquals(5, allUser.size());
    }

    @Test
    @DisplayName("Test creat Table User")
    void createTableTest() {
        userDao.createTable();
        boolean creatTrue = userDao.checkIfTableExists("users");
        assertTrue(creatTrue);
        boolean noCreat = userDao.checkIfTableExists("pusers");
        assertFalse(noCreat);
    }

    @Test
    @DisplayName("Test getUser")
    void getUserTest() {
        int userId = userDao.getAll().get(4).getId();
        assertEquals(5, userId);
        assertEquals("eminem", userDao.get(5).getUserName());
        assertNull(userDao.get(8));
    }

    @Test
    @DisplayName("Test update User")
    void updateUserTest() {
        int userId = userDao.getAll().get(3).getId();
        User user = User.builder()
                .userName("Jace")
                .lastName("Galcin")
                .firstName("Gena")
                .passport(56987415)
                .email("gavril@mail.ru")
                .bankCard(32569874125463L)
                .build();
        assertEquals("gav@mail.ru", userDao.get(userId).getEmail());
        userDao.update(userId, user);
        assertEquals("gavril@mail.ru", userDao.get(userId).getEmail());
        assertNull(userDao.update(9, user));
    }

    @Test
    @DisplayName("Test save and delete")
    void saveTest() {
        User user = User.builder()
                .userName("vandervud")
                .lastName("Tom")
                .firstName("Hardi")
                .passport(85234789)
                .email("hardi@mail.ru")
                .bankCard(258963214785L)
                .build();
        User newUser = userDao.save(user);
        assertNotNull(newUser);
        assertEquals(6, userDao.getAll().size());
        assertEquals("vandervud", userDao.getAll().get(5).getUserName());
        int idUser = userDao.getAll().get(5).getId();

        assertAll(
                () -> assertEquals(6, idUser),
                () ->  assertTrue(userDao.delete(idUser)),
                () -> assertFalse(userDao.delete(9)),
                () -> assertEquals(5, userDao.getAll().size()),
                () -> assertFalse(userDao.getAll().stream().anyMatch(u -> u.getUserName().equals("vandervud")))
        );
    }

    @Test
    @DisplayName("Test filtrUser")
    void filtrUserTest() {
        Predicate<User> predicate = c -> c.getUserName().contains("jerry");
        List<User> filteredUser = userDao.filterBy(predicate);
        assertEquals("jerry", filteredUser.get(0).getUserName(), "name должен быть 'jerry'");
        filteredUser.forEach(System.out::println);
    }
}
