package ru.rental.servic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.rental.servic.config.RentalConfig;
import ru.rental.servic.dto.UserDto;
import ru.rental.servic.service.CarService;
import ru.rental.servic.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class UserApp {
    private static final String CRUD = RentalConfig.getPropertyMenu("crud");
    private final Scanner scanner;
    private final UserService userService;

    @Autowired
    public UserApp(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void crudUser() {
        while (true) {
            List<UserDto> users = userService.getAll();
            for (UserDto user : users) {
                System.out.println(user);
            }
            System.out.println(CRUD);
            String scan = scanner.nextLine();

            if (scan.equalsIgnoreCase("1")) {
                System.out.println("Введите id пользователя: ");
                scan = scanner.nextLine();
                int number = Integer.parseInt(scan);
                var user = userService.get(number);
                System.out.println(user);

            } else if (scan.equalsIgnoreCase("2")) {
                System.out.println("Введите id пользователя для обновления: ");
                String idInput = scanner.nextLine();
                try {
                    Integer id = Integer.parseInt(idInput);
                    Optional<UserDto> maybeUser = userService.get(id);
                    if (maybeUser.isPresent()) {
                        UserDto userToUpdate = maybeUser.get();
                        System.out.println("Введите новое имя пользователя (текущее: " + userToUpdate.getUserName() + "):");
                        String userName = scanner.nextLine();

                        System.out.println("Введите новое имя (текущее: " + userToUpdate.getFirstName() + "):");
                        String firstName = scanner.nextLine();

                        System.out.println("Введите новую фамилию (текущая: " + userToUpdate.getLastName() + "):");
                        String lastName = scanner.nextLine();

                        System.out.println("Введите новый паспорт (текущий: " + userToUpdate.getPassport() + "):");
                        int passport = Integer.parseInt(scanner.nextLine());

                        System.out.println("Введите новый email (текущий: " + userToUpdate.getEmail() + "):");
                        String email = scanner.nextLine();

                        System.out.println("Введите новый номер банковской карты (текущий: " + userToUpdate.getBankCard() + "):");
                        long bankCard = Long.parseLong(scanner.nextLine());

                        UserDto updatedUser = new UserDto(id, userName, firstName, lastName, passport, email, bankCard);

                        Optional<UserDto> result = userService.update(id, updatedUser);

                        if (result.isPresent()) {
                            System.out.println("Пользователь успешно обновлен: " + result.get());
                        } else {
                            System.out.println("Не удалось обновить пользователя.");
                        }
                    } else {
                        System.out.println("Пользователь с таким ID не найден.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введены некорректные данные.");
                }
            } else if (scan.equalsIgnoreCase("3")) {
                System.out.println("Введите имя нового пользователя:");
                String userName = scanner.nextLine();

                System.out.println("Введите имя нового пользователя:");
                String firstName = scanner.nextLine();

                System.out.println("Введите фамилию нового пользователя:");
                String lastName = scanner.nextLine();

                System.out.println("Введите паспорт нового пользователя:");
                int passport = Integer.parseInt(scanner.nextLine());

                System.out.println("Введите email нового пользователя:");
                String email = scanner.nextLine();

                System.out.println("Введите номер банковской карты нового пользователя:");
                long bankCard = Long.parseLong(scanner.nextLine());

                UserDto newUser = new UserDto(0, userName, firstName, lastName, passport, email, bankCard);

                UserDto savedUser = userService.save(newUser);

                if (savedUser != null) {
                    System.out.println("Новый пользователь успешно сохранен: " + savedUser);
                } else {
                    System.out.println("Не удалось сохранить пользователя.");
                }
            } else if (scan.equalsIgnoreCase("4")) {
                System.out.println("Введите id пользователя для удаления: ");
                String idInput = scanner.nextLine();
                try {
                    Integer id = Integer.parseInt(idInput);
                    boolean isDeleted = userService.delete(id);

                    if (isDeleted) {
                        System.out.println("Пользователь успешно удален.");
                    } else {
                        System.out.println("Пользователь с таким ID не найден или не удалось удалить.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введены некорректные данные.");
                }
            } else if (scan.equalsIgnoreCase("5")) {
                System.out.println("Введите фильтр для пользователей в формате: поле условие значение (например, email = 'example@gmail.com'):");
                System.out.println("Фильтрация не доступна!!!");
                break;

            } else if (scan.equalsIgnoreCase("6")) {
                break;
            }
        }
    }

    public void carUser() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RentalServiceApp.class);
        final var carService = context.getBean("carService", CarService.class);
        System.out.println("Тестируем добавления машины!!!");
        System.out.println("Введите id user: ");
        int userId = scanner.nextInt();
        System.out.println("Введите id car");
        int carId = scanner.nextInt();
        carService.assignUserToCar(carId, userId);
        System.out.println("Авто добавленно");
    }
}
