package rental.servic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.rental.service.dao.UserDao;
import ru.rental.service.model.User;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest extends BaseBd {

    private static final UserDao USER_DAO = new UserDao();

    private static Stream<Arguments> sourceUser() {
        return Stream.of(
                Arguments.of(
                        User.builder()
                                .id(1)
                                .userName("bill")
                                .firstName("Ivanov")
                                .lastName("Dima")
                                .passport(456987123)
                                .email(null)
                                .bankCard(7896541236547852L)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(2)
                                .userName("tom")
                                .firstName("Sidorov")
                                .lastName("Pasha")
                                .passport(98741236)
                                .email(null)
                                .bankCard(987456321458796L)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(3)
                                .userName("jerry")
                                .firstName("Petrov")
                                .lastName("Sasha")
                                .passport(12365478)
                                .email(null)
                                .bankCard(12589745321698L)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(4)
                                .userName("ozi")
                                .firstName("Galcin")
                                .lastName("Gena")
                                .passport(56987415)
                                .email("gav@mail.ru")
                                .bankCard(32569874125463L)
                                .build()
                ),
                Arguments.of(
                        User.builder()
                                .id(5)
                                .userName("eminem")
                                .firstName("Pugachev")
                                .lastName("Genya")
                                .passport(85297418)
                                .email(null)
                                .bankCard(943655557412365L)
                                .build()
                )
        );
    }

    private static Stream<Arguments> sourceUserForFilterTest() {
        return Stream.of(
                Arguments.of(new User(1, "bill", "Ivanov", "Dima", 456987123,
                        null, 7896541236547852L, null, null), "bill", "bill"),
                Arguments.of(new User(2, "tom", "Sidorov", "Pasha", 98741236, null,
                        987456321458796L, null, null), "tom", "tom"),
                Arguments.of(new User(3, "jerry", "Petrov", "Sasha", 12365478, null,
                        12589745321698L, null, null), "jerry", "jerry"),
                Arguments.of(new User(4, "ozi", "Galcin", "Gena", 56987415,
                        "gav@mail.ru", 32569874125463L, null, null), "ozi", "ozi"),
                Arguments.of(new User(5, "eminem", "Pugachev", "Genya", 85297418,
                        null, 943655557412365L, null, null), "eminem", "eminem")
        );
    }

    @Test
    @DisplayName("Test getAllUser")
    void userDaoGetAll() {
        final var allUsers = USER_DAO.getAll();
        assertEquals(5, allUsers.size());
    }

    @Test
    @DisplayName("Test creat Table Users")
    void createTableTest() {
        USER_DAO.createTable();
        boolean creatTrue = USER_DAO.checkIfTableExists("users");
        assertTrue(creatTrue);
        boolean noCreat = USER_DAO.checkIfTableExists("test");
        assertFalse(noCreat);
    }

    @ParameterizedTest
    @MethodSource("sourceUser")
    @DisplayName("Test getUser")
    void getUserTest(User sourceUser) {
        final var user = USER_DAO.get(sourceUser.getId());

        assertAll(
                () -> assertEquals(user.getId(), sourceUser.getId()),
                () -> assertEquals(user.getUserName(), sourceUser.getUserName()),
                () -> assertEquals(user.getFirstName(), sourceUser.getFirstName()),
                () -> assertEquals(user.getLastName(), sourceUser.getLastName()),
                () -> assertEquals(user.getPassport(), sourceUser.getPassport()),
                () -> assertEquals(user.getEmail(), sourceUser.getEmail()),
                () -> assertEquals(user.getBankCard(), sourceUser.getBankCard())
        );
    }

    @ParameterizedTest
    @DisplayName("Test update user")
    @MethodSource("sourceUser")
    void updateUserTest(User sourceUser) {

        int uniquePassport = generateUniquePassport();

        User updatedUser = new User(
                sourceUser.getId(),
                sourceUser.getUserName(),
                sourceUser.getFirstName(),
                sourceUser.getLastName(),
                uniquePassport,
                sourceUser.getEmail(),
                sourceUser.getBankCard(),
                sourceUser.getListBike(),
                sourceUser.getListCar()
        );

        User nonUser = new User(
                -1,
                sourceUser.getUserName(),
                sourceUser.getFirstName(),
                sourceUser.getLastName(),
                sourceUser.getPassport(),
                sourceUser.getEmail(),
                sourceUser.getBankCard(),
                sourceUser.getListBike(),
                sourceUser.getListCar()
        );

        int nonUserId = nonUser.getId();

        final var updatedUserBd = USER_DAO.update(sourceUser.getId(), updatedUser);
        final var userUpdate = USER_DAO.get(updatedUserBd.getId());

        assertAll(
                () -> assertEquals(userUpdate.getId(), updatedUser.getId()),
                () -> assertEquals(userUpdate.getUserName(), updatedUser.getUserName()),
                () -> assertEquals(userUpdate.getFirstName(), updatedUser.getFirstName()),
                () -> assertEquals(userUpdate.getLastName(), updatedUser.getLastName()),
                () -> assertEquals(userUpdate.getPassport(), updatedUser.getPassport()),
                () -> assertEquals(userUpdate.getEmail(), updatedUser.getEmail()),
                () -> assertEquals(userUpdate.getBankCard(), updatedUser.getBankCard()),
                () -> assertThrows(IllegalStateException.class, () -> {
                    USER_DAO.update(nonUserId, updatedUser);
                }, "Expected update to throw an exception as the user does not exist")
        );
    }

    @ParameterizedTest
    @MethodSource("sourceUser")
    @DisplayName("Test save and delete user")
    void saveAndDeleteUserTest(User sourceUser) {
        final var userDao = USER_DAO;
        int uniquePassport = generateUniquePassport();

        User userToSave = new User(
                -896,
                sourceUser.getUserName(),
                sourceUser.getFirstName(),
                sourceUser.getLastName(),
                uniquePassport,
                sourceUser.getEmail(),
                sourceUser.getBankCard(),
                sourceUser.getListBike(),
                sourceUser.getListCar()
        );

        User savedUser = userDao.save(userToSave);
        assertNotNull(savedUser, "User должен быть успешно сохранен");

        assertEquals(6, userDao.getAll().size(), "После сохранения в базе должно быть 6 users");
        assertTrue(userDao.getAll().stream().anyMatch(b -> b.getId() == savedUser.getId()), "Сохраненный user должен быть в списке");

        User savedUserFromDb = userDao.get(savedUser.getId());
        assertAll(
                () -> assertEquals(userToSave.getUserName(), savedUserFromDb.getUserName(), "Имя user должно совпадать"),
                () -> assertEquals(userToSave.getFirstName(), savedUserFromDb.getFirstName(), "firstName должен совпадать"),
                () -> assertEquals(userToSave.getLastName(), savedUserFromDb.getLastName(), "LastName должен совпадать"),
                () -> assertEquals(userToSave.getPassport(), savedUserFromDb.getPassport(), "Номер Passport должен совпадать"),
                () -> assertEquals(userToSave.getEmail(), savedUserFromDb.getEmail(), "Email должен совпадать"),
                () -> assertEquals(userToSave.getBankCard(), savedUserFromDb.getBankCard(), "bankCard user должен совпадать")
        );

        assertTrue(userDao.delete(savedUser.getId()), "User должен быть успешно удален");
        assertFalse(userDao.delete(999), "Попытка удаления несуществующего user должна вернуть false");
        assertEquals(5, userDao.getAll().size(), "После удаления user в базе должно быть 5 users");
    }

    @ParameterizedTest
    @MethodSource("sourceUserForFilterTest")
    @DisplayName("Test filterUser")
    void filtrUserTest(User sourceUserForFilterTest, String filterKeyword, String expectedName) {
        final var userDao = USER_DAO;

        Predicate<User> predicate = user -> user.getUserName().contains(filterKeyword);

        List<User> filteredUsers = userDao.filterBy(predicate);

        assertTrue(filteredUsers.stream().anyMatch(b -> b.getUserName().equals(expectedName)),
                "Отфильтрованный user должен быть " + expectedName);
    }

    private int generateUniquePassport() {
        Random random = new Random();
        return random.nextInt(1000000);
    }
}
