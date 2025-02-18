package ru.rental.service.crudApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.rental.service.RentalServiceApp;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.dto.CarDto;
import ru.rental.service.dto.UserDto;
import ru.rental.service.service.BikeService;
import ru.rental.service.service.CarService;
import ru.rental.service.service.UserService;

import java.util.List;
import java.util.Scanner;

@Component
public class UserCLI extends CLI<UserDto> {

    @Autowired
    public UserCLI(Scanner scanner, UserService userService) {
        super(scanner, userService);
    }

    @Override
    public UserDto getUpdatedEntityInfo(UserDto userToUpdate) {
        System.out.println("Введите новое прозвище пользователя (текущее: " + userToUpdate.getUserName() + "):");
        String userName = getScanner().nextLine();

        System.out.println("Введите новое имя (текущее: " + userToUpdate.getFirstName() + "):");
        String firstName = getScanner().nextLine();

        System.out.println("Введите новую фамилию (текущая: " + userToUpdate.getLastName() + "):");
        String lastName = getScanner().nextLine();

        System.out.println("Введите новый паспорт (текущий: " + userToUpdate.getPassport() + "):");
        int passport = Integer.parseInt(getScanner().nextLine());

        System.out.println("Введите новый email (текущий: " + userToUpdate.getEmail() + "):");
        String email = getScanner().nextLine();

        System.out.println("Введите новый номер банковской карты (текущий: " + userToUpdate.getBankCard() + "):");
        long bankCard = Long.parseLong(getScanner().nextLine());

        return new UserDto(userToUpdate.getId(), userName, firstName, lastName, passport, email, bankCard, null, null);
    }

    @Override
    public UserDto getNewEntityInfo() {
        System.out.println("Введите прозвище нового пользователя:");
        String userName = getScanner().nextLine();

        System.out.println("Введите имя нового пользователя:");
        String firstName = getScanner().nextLine();

        System.out.println("Введите фамилию нового пользователя:");
        String lastName = getScanner().nextLine();

        System.out.println("Введите паспорт нового пользователя:");
        int passport = Integer.parseInt(getScanner().nextLine());

        System.out.println("Введите email нового пользователя:");
        String email = getScanner().nextLine();

        System.out.println("Введите номер банковской карты нового пользователя:");
        long bankCard = Long.parseLong(getScanner().nextLine());

        return new UserDto(0, userName, firstName, lastName, passport, email, bankCard, null, null);
    }

    public void addCarAndBikeUser() {
        ApplicationContext context = new AnnotationConfigApplicationContext(RentalServiceApp.class);
        final var carService = context.getBean("carService", CarService.class);
        final var bikeService = context.getBean("bikeService", BikeService.class);

        System.out.println("Выберите транспорт: ");
        System.out.println("1. Добавить машину");
        System.out.println("2. Добавить байк");
        String choice = getScanner().nextLine();

        System.out.println("Введите id пользователя: ");
        int userId = getScanner().nextInt();

        if ("1".equals(choice)) {
            System.out.println("Введите id машины: ");
            int carId = getScanner().nextInt();
            carService.updateUserId(carId, userId);
            System.out.println("Авто добавлено");

        } else if ("2".equals(choice)) {
            System.out.println("Введите id байка: ");
            int bikeId = getScanner().nextInt();
            bikeService.updateUserId(bikeId, userId);
            System.out.println("Байк добавлен");

        } else {
            System.out.println("Неверный выбор. Пожалуйста, выберите 1 для машины или 2 для байка.");
        }

        getScanner().nextLine();
    }
}
