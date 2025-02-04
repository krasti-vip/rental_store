package ru.rental.servic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.rental.servic.config.RentalConfig;
import ru.rental.servic.service.BikeService;
import ru.rental.servic.service.CarService;
import ru.rental.servic.service.UserService;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "ru.rental.servic")
public class RentalServiceApp {

    public final static String MAIN_MENU = RentalConfig.getPropertyMenu("menu");
    public final static String ENTER = RentalConfig.getPropertyMenu("enter");

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ApplicationContext context = new AnnotationConfigApplicationContext(RentalServiceApp.class);
        final var bikeService = context.getBean("bikeService", BikeService.class);
        final var carService = context.getBean("carService", CarService.class);
        final var userService = context.getBean("userService", UserService.class);
        final var bikeApp = context.getBean(BikeApp.class);
        final var userApp = context.getBean(UserApp.class);
        final var carApp = context.getBean(CarApp.class);

        System.out.println(ENTER);

        while (true) {
            String password = scanner.nextLine();
            if (password.equalsIgnoreCase("admin")) {
                while (true) {
                    System.out.println(MAIN_MENU);
                    String scan = scanner.nextLine();
                    if (scan.equalsIgnoreCase("1")) {
                        userApp.carUser();
                        System.out.println("Для получения списка авто user введите его id: ");
                        int userId = scanner.nextInt();
                        carService.getCarsByUser(userId).forEach(System.out::println);
                        break;

                    } else if (scan.equalsIgnoreCase("2")) {
                        bikeApp.crudBike();

                    } else if (scan.equalsIgnoreCase("3")) {
                        carApp.crudCar();

                    } else if (scan.equalsIgnoreCase("4")) {
                        userApp.crudUser();

                    } else if (scan.equalsIgnoreCase("5")) {
                        break;
                    }
                }

            } else if (password.equalsIgnoreCase("exit")) {
                break;

            } else {
                System.out.println("Неверный пароль");

            }
        }
    }
}


//        scanner.close();
//        System.out.println(bikeService.getAll());

//        while (true) {
//            System.out.println(MAIN_MENU);
//
//            final var target = scanner.next();

//
//        }

//        if ("bike") {
//            bikeService.getAll();
//        }


// CLI
// 1. Параметризованные тесты +
// 2. CLI CRUD
// 3. Подумай как юзеру добавить в базу машины и байки? (Many to many | one to many | many to one | one to one)
// https://habr.com/ru/articles/488054/
// https://metanit.com/sql/mysql/2.5.php

