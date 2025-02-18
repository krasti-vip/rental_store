package ru.rental.service.crudApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dto.CarDto;
import ru.rental.service.service.CarService;
import java.util.Scanner;

@Component
public class CarCLI extends CLI<CarDto> {

    @Autowired
    public CarCLI(Scanner scanner, CarService carService) {
        super(scanner, carService);
    }

    @Override
    public CarDto getUpdatedEntityInfo(CarDto carToUpdate) {
        System.out.println("Введите новое название автомобиля (текущее: " + carToUpdate.getTitle() + "):");
        String title = getScanner().nextLine();

        System.out.println("Введите новую цену автомобиля (текущая: " + carToUpdate.getPrice() + "):");
        double price = Double.parseDouble(getScanner().nextLine());

        System.out.println("Введите новый объем автомобиля (текущий: " + carToUpdate.getVolume() + "):");
        double volume = Double.parseDouble(getScanner().nextLine());

        System.out.println("Введите новую мощность автомобиля (текущая: " + carToUpdate.getHorsePower() + "):");
        int horsePower = Integer.parseInt(getScanner().nextLine());

        System.out.println("Введите новый цвет автомобиля (текущий: " + carToUpdate.getColor() + "):");
        String color = getScanner().nextLine();

        return new CarDto(carToUpdate.getId(), title, price, horsePower, volume, color, null);
    }

    @Override
    public CarDto getNewEntityInfo() {
        System.out.println("Введите название нового автомобиля:");
        String title = getScanner().nextLine();

        System.out.println("Введите цену нового автомобиля:");
        double price = Double.parseDouble(getScanner().nextLine());

        System.out.println("Введите объем нового автомобиля:");
        double volume = Double.parseDouble(getScanner().nextLine());

        System.out.println("Введите мощность нового автомобиля:");
        int horsePower = Integer.parseInt(getScanner().nextLine());

        System.out.println("Введите цвет нового автомобиля:");
        String color = getScanner().nextLine();

        return new CarDto(0, title, price, horsePower, volume, color, null);
    }
}

