package ru.rental.servic;

import org.springframework.context.annotation.*;
import ru.rental.servic.config.RentalConfig;
import ru.rental.servic.dto.BikeDto;
import ru.rental.servic.dto.CarDto;
import ru.rental.servic.service.BikeService;
import ru.rental.servic.service.CarService;
import ru.rental.servic.service.UserService;
import java.util.List;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "ru.rental.servic")
public class RentalServiceApp {

    public final static String MAIN_MENU = RentalConfig.getPropertyMenu("menu");
    public final static String ENTER = RentalConfig.getPropertyMenu("enter");
    public final static String RENTAL = RentalConfig.getPropertyMenu("rental");

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RentalServiceApp.class);


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
                        userApp.carAndBikeUser();

                    } else if (scan.equalsIgnoreCase("2")) {
                        System.out.println("Введите id user: ");
                        int userId1 = scanner.nextInt();
                        scanner.nextLine();
                        System.out.println("Выберите 1 если хотите просмотреть список машин или 2 для просмотра списка байков: ");
                        String bikeAndCar = scanner.nextLine();
                        switch (bikeAndCar.toLowerCase()) {
                            case "1":
                                List<CarDto> cars = carService.getAllByUserId(userId1);
                                if (cars.isEmpty()) {
                                    System.out.println("У пользователя нет автомобилей.");
                                } else {
                                    cars.forEach(car -> System.out.println(car));  //
                                }
                                break;
                            case "2":
                                List<BikeDto> bikes = bikeService.getAllByUserId(userId1);
                                if (bikes.isEmpty()) {
                                    System.out.println("У пользователя нет байков.");
                                } else {
                                    bikes.forEach(bike -> System.out.println(bike));  // выводим каждый объект
                                }
                                break;
                            case "3":
                                return;
                            default:
                                System.out.println("Неверный выбор. Попробуйте еще раз.");
                        }
                        continue;

                    } else if (scan.equalsIgnoreCase("3")) {
                        bikeApp.crudBike();

                    } else if (scan.equalsIgnoreCase("4")) {
                        carApp.crudCar();

                    } else if (scan.equalsIgnoreCase("5")) {
                        userApp.crudUser();

                    } else if (scan.equalsIgnoreCase("6")) {
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




