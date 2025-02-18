package ru.rental.service.crudApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.dto.BikeDto;
import ru.rental.service.service.BikeService;
import java.util.Scanner;

@Component
public class BikeCLI extends CLI<BikeDto> {

    @Autowired
    public BikeCLI(Scanner scanner, BikeService bikeService) {
        super(scanner, bikeService);
    }

    @Override
    public BikeDto getUpdatedEntityInfo(BikeDto bikeToUpdate) {
        System.out.println("Введите новое имя байка (текущее: " + bikeToUpdate.getName() + "):");
        String name = getScanner().nextLine();

        System.out.println("Введите новую цену байка (текущая: " + bikeToUpdate.getPrice() + "):");
        double price = Double.parseDouble(getScanner().nextLine());

        System.out.println("Введите новый объем байка (текущий: " + bikeToUpdate.getVolume() + "):");
        double volume = Double.parseDouble(getScanner().nextLine());

        System.out.println("Введите новую мощность байка (текущая: " + bikeToUpdate.getHorsePower() + "):");
        int horsePower = Integer.parseInt(getScanner().nextLine());

        return new BikeDto(bikeToUpdate.getId(), name, price, horsePower, volume, null);
    }

    @Override
    public BikeDto getNewEntityInfo() {
        System.out.println("Введите имя нового байка:");
        String name = getScanner().nextLine();

        System.out.println("Введите цену нового байка:");
        double price = Double.parseDouble(getScanner().nextLine());

        System.out.println("Введите объем нового байка:");
        double volume = Double.parseDouble(getScanner().nextLine());

        System.out.println("Введите мощность нового байка:");
        int horsePower = Integer.parseInt(getScanner().nextLine());

        return new BikeDto(0, name, price, horsePower, volume, null);
    }
}
