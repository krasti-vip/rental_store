package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.UserDao;
import ru.rental.servic.model.User;

import java.util.List;

public class UserDaoTest {

    UserDao userDao = new UserDao();
    List<User> bd = userDao.getAll();

    @Test
    @DisplayName("Проверка геттера")
    public void getTest() {
        Assertions.assertEquals("Жонный Пит", bd.get(2).getUserName(),
                "Должен быть Жонный пит");
    }

    @Test
    @DisplayName("Проверка обновления")
    public void updateTest() {
        User newUser = new User("Падший Ангел", "Иванов", "Федя", 328569, "angelDead@mail.com",
                7894);
        User result = userDao.update("Падший Ангел", newUser);
        Assertions.assertNotNull(result, "Падший ангел должен быть");
        List<User> bd = userDao.getAll();
        Assertions.assertEquals("angelDead@mail.com", bd.get(0).getEmail(), "Должен буть " +
                "angelDead@mail.comр");
    }

    @Test
    @DisplayName("Проверка добавления юзера")
    public void saveTest() {
        User newUser = new User("Телепузик", "Иванофф", "Федя", 998569, "angelDead@mail.com",
                9994);
        User result = userDao.save(newUser);
        List<User> bd = userDao.getAll();
        Assertions.assertEquals("Телепузик", bd.get(10).getUserName(), "Должен быть" +
                "Телепузик");
        Assertions.assertEquals(11, bd.size(), "Должно быть 11 байков");
    }

    @Test
    @DisplayName("Проверка удаления юзера")
    public void deleteTest() {
        Assertions.assertEquals(10, bd.size(), "Изначально лист должен быть длиной 10");
        boolean deleteUser = userDao.delete("Орел пустыни");
        List<User> bd = userDao.getAll();
        Assertions.assertTrue(deleteUser, "юзер должен быть удален");
        Assertions.assertEquals(9, bd.size(), "Лист должен быть длиной 9");
    }

    @Test
    @DisplayName("Проверка фильтрации")
    public void filterUserTest() {
        List<User> filteredUsers = userDao.filterBy(user -> user.getUserName().length() > 10);
        for (User user : filteredUsers) {
            Assertions.assertTrue(user.getUserName().length() > 10,
                    "Все отфильтрованные пользователи должны иметь имя длиннее 10 символов");
        }
    }
}
