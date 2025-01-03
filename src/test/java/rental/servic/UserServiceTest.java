package test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.UserDao;
import ru.rental.servic.model.User;
import java.util.List;

public class UserServiceTest {

    UserDao userDao = new UserDao();
    List<User> bd = userDao.getAll();

    @BeforeEach
    public void setUp() {
        bd = userDao.getAll();
    }

    @Test
    @DisplayName("Проверка геттера")
    public void getTest() {
        Assertions.assertEquals("Саша", userDao.get("Жонный Пит").getLastName(), "должен быть Саша");
        Assertions.assertNotEquals("Саша", userDao.get("Падший Ангел").getLastName(), "должен быть Федя");
    }

    @Test
    @DisplayName("Проверка обновления user по id")
    public void updateTest() {
        userDao.update("Жонный Пит", new User("Жонный Пит", "Петров", "Саша",
                456982, "pit@mail.com", 482678));
        Assertions.assertEquals(482678, userDao.get("Жонный Пит").getBankCard(), "Должен быть 482678");
    }

    @Test
    @DisplayName("Проверка создания нового user")
    public void saveTest() {
        userDao.save(new User("11ый", "Тимур", "Гдиев", 85789, "11@mail.ru", 785236));
        List<User> update = userDao.getAll();
        Assertions.assertEquals(11, update.size(), "должно быть 11");
        Assertions.assertEquals("angel@mail.com", userDao.get("Падший Ангел").getEmail(), "должен быть Порше");
        Assertions.assertEquals("Гдиев", userDao.get("11ый").getLastName(), "Должен быть Гдиев");
    }

    @Test
    @DisplayName("Проверка удаления")
    public void deleteTest() {
        userDao.delete("Дохлый шторм");
        List<User> delete = userDao.getAll();
        Assertions.assertEquals(9, delete.size());
        Assertions.assertNull(userDao.get("Дохлый шторм"));
    }

    @Test
    @DisplayName("Проверка фильтра")
    public void filtrTest() {
        List<User> bdFiltr = userDao.filterBy(user -> user.getUserName().startsWith("С"));
        Assertions.assertEquals(3, bdFiltr.size(), "должно быть 3 нормальных user");
   }
}
