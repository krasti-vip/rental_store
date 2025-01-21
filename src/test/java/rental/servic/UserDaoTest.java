package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.UserDao;

public class UserDaoTest {

    private final UserDao userDao = new UserDao();

    @Test
    @DisplayName("Тестирование создания таблицы")
    void createTableTest() {
        userDao.createTable();
    }

    @Test
    @DisplayName("Тестирование выборки данных из таблицы users")
    void selectUserTest() {
        final var user = userDao.get("");
        System.out.println(user);
    }
}
