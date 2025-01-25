package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.rental.servic.dao.UserDao;
import ru.rental.servic.model.User;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;


class UserDaoTest {

    @Test
    @DisplayName("Тестирование всей таблицы user")
    void userTest() {
        UserDao userDao = new UserDao();
        userDao.createTable();
        boolean tableExists = userDao.checkIfTableExists("users");
        assertTrue(tableExists);

        User user = User.builder()
                .userName("Jaccoco")
                .firstName("Boyko")
                .lastName("Dima")
                .passport(72698521)
                .bankCard(1234_4569_9874_1236L)
                .build();

        User savedUser = userDao.save(user);
        assertNotNull(savedUser);
        assertEquals("Jaccoco", savedUser.getUserName());

        Integer savedUserId = savedUser.getId();
        System.out.println(savedUserId);
        System.out.println(savedUser);
        assertEquals(userDao.get(savedUserId), savedUser, "что то не так c тестом get");

        User user2 = User.builder()
                .userName("Jaccoco")
                .firstName("Boyko")
                .lastName("Dima")
                .passport(72698521)
                .email("jaccoco@gmail.com")
                .bankCard(1234_4569_9874_1236L)
                .build();

        userDao.update(user.getId(), user2);
        assertEquals("jaccoco@gmail.com", userDao.get(savedUserId).getEmail());

        assertNotNull(user, "юзер должен быть");
        userDao.delete(user.getId());
        assertNull(userDao.get(user.getId()), "юзер должен быть удален");

        List<User> bd = userDao.getAllUsers();
        bd.forEach(System.out::println);
        assertNotNull(bd);

  //      userDao.deleteAllUsers();
        List<User> bd2 = userDao.getAllUsers();
        assertFalse(bd2.isEmpty());

        User user3 = User.builder()
                .userName("Jaccoco")
                .firstName("Boyko")
                .lastName("Dima")
                .passport(72698521)
                .email("jaccoco@gmail.com")
                .bankCard(1234_4569_9874_1236L)
                .build();

        User user4 = User.builder()
                .userName("Maven")
                .firstName("Man")
                .lastName("Boy")
                .passport(1236547)
                .email("jaccoco@gmail.com")
                .bankCard(9874_1236_1234_4569L)
                .build();

        userDao.save(user3);
        userDao.save(user4);

        Predicate<User> lastNamePredicate = boy -> boy.getLastName().contains("Boy");

        List<User> filteredUsers = userDao.filterBy(lastNamePredicate);

 //       assertEquals(1, filteredUsers.size(), "Должен быть найден только один пользователь с фамилией 'Boy'");
        assertEquals("Boy", filteredUsers.get(0).getLastName(), "Фамилия должна быть 'Boy'");

        filteredUsers.forEach(System.out::println);
    }
}
