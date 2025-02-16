package ru.rental.service.crudApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.rental.service.RentalServiceApp;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.BikeService;
import ru.rental.service.service.CarService;
import ru.rental.service.service.UserService;
import ru.rental.service.util.PropertiesUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class UserCRUD {

    private static final String CRUD = PropertiesUtil.getPropertyMenu("crud");
    private final Scanner scanner;
    private final UserService userService;

    @Autowired
    public UserCRUD(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public void crudUser() {
        while (true) {
            displayAllUsers();
            System.out.println(CRUD);
            String scan = scanner.nextLine();

            switch (scan.toLowerCase()) {
                case "1":
                    getUserById();
                    break;
                case "2":
                    updateUser();
                    break;
                case "3":
                    createUser();
                    break;
                case "4":
                    deleteUser();
                    break;
                case "5":
                    System.out.println("Фильтрация не доступна!!!");
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Неверный выбор. Попробуйте еще раз.");
            }
        }
    }

    private void displayAllUsers() {
        List<UserDto> users = userService.getAll();
        for (UserDto user : users) {
            System.out.println(user);
        }
    }

    private void getUserById() {
        System.out.println("Введите id пользователя: ");
        String scan = scanner.nextLine();
        try {
            int number = Integer.parseInt(scan);
            var user = userService.get(number);
            System.out.println(user);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введен некорректный ID.");
        }
    }

    private void updateUser() {
        System.out.println("Введите id пользователя для обновления: ");
        String idInput = scanner.nextLine();
        try {
            Integer id = Integer.parseInt(idInput);
            Optional<UserDto> maybeUser = userService.get(id);
            if (maybeUser.isPresent()) {
                UserDto userToUpdate = maybeUser.get();
                String userName = inputUpdateData("имя пользователя", userToUpdate.getUserName());
                String firstName = inputUpdateData("имя", userToUpdate.getFirstName());
                String lastName = inputUpdateData("фамилию", userToUpdate.getLastName());
                int passport = Integer.parseInt(inputUpdateData("паспорт", String.valueOf(userToUpdate.getPassport())));
                String email = inputUpdateData("email", userToUpdate.getEmail());
                long bankCard = Long.parseLong(inputUpdateData("номер банковской карты", String.valueOf(userToUpdate.getBankCard())));

                UserDto updatedUser = new UserDto(id, userName, firstName, lastName, passport, email, bankCard, null, null);

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
    }

    private void createUser() {
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

        UserDto newUser = new UserDto(0, userName, firstName, lastName, passport, email, bankCard, null, null);

        UserDto savedUser = userService.save(newUser);

        if (savedUser != null) {
            System.out.println("Новый пользователь успешно сохранен: " + savedUser);
        } else {
            System.out.println("Не удалось сохранить пользователя.");
        }
    }

    private void deleteUser() {
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
    }

    private String inputUpdateData(String fieldName, String currentValue) {
        System.out.println("Введите новое " + fieldName + " (текущее: " + currentValue + "):");
        return scanner.nextLine();
    }

    public void carAndBikeUser() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RentalServiceApp.class);
        final var carService = context.getBean("carService", CarService.class);
        final var bikeService = context.getBean("bikeService", BikeService.class);

        System.out.println("Выберите транспорт: ");
        System.out.println("1. Добавить машину");
        System.out.println("2. Добавить байк");
        String choice = scanner.nextLine();

        System.out.println("Введите id пользователя: ");
        int userId = scanner.nextInt();

        if ("1".equals(choice)) {
            System.out.println("Введите id машины: ");
            int carId = scanner.nextInt();
            carService.updateUserId(carId, userId);
            System.out.println("Авто добавлено");

        } else if ("2".equals(choice)) {
            System.out.println("Введите id байка: ");
            int bikeId = scanner.nextInt();
            bikeService.updateUserId(bikeId, userId);
            System.out.println("Байк добавлен");

        } else {
            System.out.println("Неверный выбор. Пожалуйста, выберите 1 для машины или 2 для байка.");
        }

        scanner.nextLine();
    }
}
