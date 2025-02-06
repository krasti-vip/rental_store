package ru.rental.servic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.servic.config.RentalConfig;
import ru.rental.servic.dto.CarDto;
import ru.rental.servic.service.CarService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class CarApp {

    private static final String CRUD = RentalConfig.getPropertyMenu("crud");
    private final Scanner scanner;
    private final CarService carService;

    @Autowired
    public CarApp(CarService carService) {
        this.carService = carService;
        this.scanner = new Scanner(System.in);
    }

    public void crudCar() {
        while (true) {
            List<CarDto> cars = carService.getAll();
            for (CarDto car : cars) {
                System.out.println(car);
            }
            System.out.println(CRUD);
            String scan = scanner.nextLine();

            if (scan.equalsIgnoreCase("1")) {
                System.out.println("Введите id автомобиля: ");
                scan = scanner.nextLine();
                int number = Integer.parseInt(scan);
                var car = carService.get(number);
                System.out.println(car);

            } else if (scan.equalsIgnoreCase("2")) {
                System.out.println("Введите id автомобиля для обновления: ");
                String idInput = scanner.nextLine();
                try {
                    Integer id = Integer.parseInt(idInput);
                    Optional<CarDto> maybeCar = carService.get(id);
                    if (maybeCar.isPresent()) {
                        CarDto carToUpdate = maybeCar.get();
                        System.out.println("Введите новое имя автомобиля (текущее: " + carToUpdate.getTitle() + "):");
                        String title = scanner.nextLine();

                        System.out.println("Введите новую цену автомобиля (текущая: " + carToUpdate.getPrice() + "):");
                        int price = Integer.parseInt(scanner.nextLine());

                        System.out.println("Введите новый объем автомобиля (текущий: " + carToUpdate.getVolume() + "):");
                        double volume = Double.parseDouble(scanner.nextLine());

                        System.out.println("Введите новую мощность автомобиля (текущая: " + carToUpdate.getHorsePower() + "):");
                        int horsePower = Integer.parseInt(scanner.nextLine());

                        System.out.println("Введите новый цвет автомобиля (текущий: " + carToUpdate.getColor() + "):");
                        String color = scanner.nextLine();

                        CarDto updatedCar = new CarDto(id, title, price, horsePower, volume, color);

                        Optional<CarDto> result = carService.update(id, updatedCar);

                        if (result.isPresent()) {
                            System.out.println("Автомобиль успешно обновлен: " + result.get());
                        } else {
                            System.out.println("Не удалось обновить автомобиль.");
                        }
                    } else {
                        System.out.println("Автомобиль с таким ID не найден.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введены некорректные данные.");
                }
            } else if (scan.equalsIgnoreCase("3")) {
                System.out.println("Введите название нового автомобиля:");
                String title = scanner.nextLine();

                System.out.println("Введите цену нового автомобиля:");
                double price = Double.parseDouble(scanner.nextLine());

                System.out.println("Введите объем нового автомобиля:");
                double volume = Double.parseDouble(scanner.nextLine());

                System.out.println("Введите мощность нового автомобиля:");
                int horsePower = Integer.parseInt(scanner.nextLine());

                System.out.println("Введите цвет нового автомобиля:");
                String color = scanner.nextLine();

                CarDto newCar = new CarDto(0, title, price, horsePower, volume, color);

                CarDto savedCar = carService.save(newCar);

                if (savedCar != null) {
                    System.out.println("Новый автомобиль успешно сохранен: " + savedCar);
                } else {
                    System.out.println("Не удалось сохранить автомобиль.");
                }
            } else if (scan.equalsIgnoreCase("4")) {
                System.out.println("Введите id автомобиля для удаления: ");
                String idInput = scanner.nextLine();
                try {
                    Integer id = Integer.parseInt(idInput);
                    boolean isDeleted = carService.delete(id);

                    if (isDeleted) {
                        System.out.println("Автомобиль успешно удален.");
                    } else {
                        System.out.println("Автомобиль с таким ID не найден или не удалось удалить.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введены некорректные данные.");
                }
            } else if (scan.equalsIgnoreCase("5")) {
                System.out.println("Введите фильтр для автомобилей в формате: поле условие значение (например, volume > 2):");
                System.out.println("Фильтрация не доступна!!!");
                break;

            } else if (scan.equalsIgnoreCase("6")) {
                break;
            }
        }
    }
}
