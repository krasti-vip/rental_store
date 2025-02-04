package ru.rental.service.crudApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dto.CarDto;
import ru.rental.service.service.CarService;
import ru.rental.service.util.PropertiesUtil;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class CarCRUD {

    private static final String CRUD = PropertiesUtil.getPropertyMenu("crud");
    private final Scanner scanner;
    private final CarService carService;

    @Autowired
    public CarCRUD(CarService carService) {
        this.carService = carService;
        this.scanner = new Scanner(System.in);
    }

    public void crudCar() {
        while (true) {
            displayCars();
            System.out.println(CRUD);
            String scan = scanner.nextLine();

            switch (scan.toLowerCase()) {
                case "1":
                    viewCarById();
                    break;
                case "2":
                    updateCar();
                    break;
                case "3":
                    addNewCar();
                    break;
                case "4":
                    deleteCar();
                    break;
                case "5":
                    filterCars();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Некорректный выбор.");
            }
        }
    }

    private void displayCars() {
        List<CarDto> cars = carService.getAll();
        for (CarDto car : cars) {
            System.out.println(car);
        }
    }

    private void viewCarById() {
        System.out.println("Введите id автомобиля: ");
        String scan = scanner.nextLine();
        try {
            int id = Integer.parseInt(scan);
            var car = carService.get(id);
            car.ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Автомобиль с таким ID не найден.")
            );
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    private void updateCar() {
        System.out.println("Введите id автомобиля для обновления: ");
        String idInput = scanner.nextLine();
        try {
            int id = Integer.parseInt(idInput);
            Optional<CarDto> maybeCar = carService.get(id);

            if (maybeCar.isPresent()) {
                CarDto carToUpdate = maybeCar.get();
                CarDto updatedCar = getUpdatedCarInfo(carToUpdate);
                Optional<CarDto> result = carService.update(id, updatedCar);

                result.ifPresentOrElse(
                        car -> System.out.println("Автомобиль успешно обновлен: " + car),
                        () -> System.out.println("Не удалось обновить автомобиль.")
                );
            } else {
                System.out.println("Автомобиль с таким ID не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    private CarDto getUpdatedCarInfo(CarDto carToUpdate) {
        System.out.println("Введите новое название автомобиля (текущее: " + carToUpdate.getTitle() + "):");
        String title = scanner.nextLine();

        System.out.println("Введите новую цену автомобиля (текущая: " + carToUpdate.getPrice() + "):");
        int price = Integer.parseInt(scanner.nextLine());

        System.out.println("Введите новый объем автомобиля (текущий: " + carToUpdate.getVolume() + "):");
        double volume = Double.parseDouble(scanner.nextLine());

        System.out.println("Введите новую мощность автомобиля (текущая: " + carToUpdate.getHorsePower() + "):");
        int horsePower = Integer.parseInt(scanner.nextLine());

        System.out.println("Введите новый цвет автомобиля (текущий: " + carToUpdate.getColor() + "):");
        String color = scanner.nextLine();

        return new CarDto(carToUpdate.getId(), title, price, horsePower, volume, color, null);
    }

    private void addNewCar() {
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

        CarDto newCar = new CarDto(0, title, price, horsePower, volume, color, null);
        CarDto savedCar = carService.save(newCar);

        if (savedCar != null) {
            System.out.println("Новый автомобиль успешно сохранен: " + savedCar);
        } else {
            System.out.println("Не удалось сохранить автомобиль.");
        }
    }

    private void deleteCar() {
        System.out.println("Введите id автомобиля для удаления: ");
        String idInput = scanner.nextLine();
        try {
            int id = Integer.parseInt(idInput);
            boolean isDeleted = carService.delete(id);

            if (isDeleted) {
                System.out.println("Автомобиль успешно удален.");
            } else {
                System.out.println("Автомобиль с таким ID не найден или не удалось удалить.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    private void filterCars() {
        System.out.println("Введите фильтр для автомобилей в формате: поле условие значение (например, volume > 2):");
        System.out.println("Фильтрация не доступна!!!");
    }
}
