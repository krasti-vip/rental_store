package ru.rental.service;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.*;
import ru.rental.service.crudApp.BikeCLI;
import ru.rental.service.crudApp.CarCLI;
import ru.rental.service.crudApp.UserCLI;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.dto.CarDto;
import ru.rental.service.service.BikeService;
import ru.rental.service.service.CarService;
import ru.rental.service.service.UserService;
import ru.rental.service.util.PropertiesUtil;
import java.util.List;
import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "ru.rental.service")
public class RentalServiceApp {

    public final static String MAIN_MENU = PropertiesUtil.getPropertyMenu("menu");

    public final static String ENTER = PropertiesUtil.getPropertyMenu("enter");

    public final static String RENTAL = PropertiesUtil.getPropertyMenu("rental");

    public static void main(String[] args) {

        final Scanner scanner = new Scanner(System.in);

        final AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RentalServiceApp.class);

        final var bikeService = context.getBean("bikeService", BikeService.class);

        final var carService = context.getBean("carService", CarService.class);

        final var userService = context.getBean("userService", UserService.class);

        final var bikeCLI = context.getBean(BikeCLI.class);

        final var userCLI = context.getBean(UserCLI.class);

        final var carCLI = context.getBean(CarCLI.class);

        System.out.println(ENTER);

        while (true) {
            String password = scanner.nextLine();
            if (password.equalsIgnoreCase("admin")) {
                while (true) {
                    System.out.println(MAIN_MENU);
                    String scan = scanner.nextLine();

                    if (scan.equalsIgnoreCase("1")) {
                        userCLI.addCarAndBikeUser();

                    } else if (scan.equalsIgnoreCase("2")) {
                        bikeCLI.crud();

                    } else if (scan.equalsIgnoreCase("3")) {
                        carCLI.crud();

                    } else if (scan.equalsIgnoreCase("4")) {
                        userCLI.crud();

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

    @Bean(name = "modelMapper")
    public ModelMapper createModelMapper() {
        return new ModelMapper();
    }
}




