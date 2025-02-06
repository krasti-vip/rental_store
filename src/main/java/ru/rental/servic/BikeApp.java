package ru.rental.servic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.servic.config.RentalConfig;
import ru.rental.servic.dto.BikeDto;
import ru.rental.servic.service.BikeService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@Component
public class BikeApp {
    private static final String CRUD = RentalConfig.getPropertyMenu("crud");
    private final Scanner scanner;
    private final BikeService bikeService;

    @Autowired
    public BikeApp(BikeService bikeService) {
        this.bikeService = bikeService;
        this.scanner = new Scanner(System.in);
    }

    public void crudBike() {
        while (true) {
            List<BikeDto> bikes = bikeService.getAll();
            for (BikeDto bike : bikes) {
                System.out.println(bike);
            }
            System.out.println(CRUD);
            String scan = scanner.nextLine();

            if (scan.equalsIgnoreCase("1")) {
                System.out.println("Введите id: ");
                scan = scanner.nextLine();
                int number = Integer.parseInt(scan);
                var bike = bikeService.get(number);
                System.out.println(bike);

            } else if (scan.equalsIgnoreCase("2")) {
                System.out.println("Введите id байка для обновления: ");
                String idInput = scanner.nextLine();
                try {
                    Integer id = Integer.parseInt(idInput);
                    Optional<BikeDto> maybeBike = bikeService.get(id);
                    if (maybeBike.isPresent()) {
                        BikeDto bikeToUpdate = maybeBike.get();
                        System.out.println("Введите новое имя байка (текущее: " + bikeToUpdate.getName() + "):");
                        String name = scanner.nextLine();

                        System.out.println("Введите новую цену байка (текущая: " + bikeToUpdate.getPrice() + "):");
                        int price = Integer.parseInt(scanner.nextLine());

                        System.out.println("Введите новый объем байка (текущий: " + bikeToUpdate.getVolume() + "):");
                        double volume = Double.parseDouble(scanner.nextLine());

                        System.out.println("Введите новую мощность байка (текущая: " + bikeToUpdate.getHorsePower() + "):");
                        int horsePower = Integer.parseInt(scanner.nextLine());

                        BikeDto updatedBike = new BikeDto(id, name, price, horsePower, volume);

                        Optional<BikeDto> result = bikeService.update(id, updatedBike);

                        if (result.isPresent()) {
                            System.out.println("Байк успешно обновлен: " + result.get());
                        } else {
                            System.out.println("Не удалось обновить байк.");
                        }
                    } else {
                        System.out.println("Байк с таким ID не найден.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введены некорректные данные.");
                }
            } else if (scan.equalsIgnoreCase("3")) {
                System.out.println("Введите имя нового байка:");
                String name = scanner.nextLine();

                System.out.println("Введите цену нового байка:");
                double price = Double.parseDouble(scanner.nextLine());

                System.out.println("Введите объем нового байка:");
                double volume = Double.parseDouble(scanner.nextLine());

                System.out.println("Введите мощность нового байка:");
                int horsePower = Integer.parseInt(scanner.nextLine());

                BikeDto newBike = new BikeDto(0, name, price, horsePower, volume);

                BikeDto savedBike = bikeService.save(newBike);

                if (savedBike != null) {
                    System.out.println("Новый байк успешно сохранен: " + savedBike);
                } else {
                    System.out.println("Не удалось сохранить байк.");
                }
            } else if (scan.equalsIgnoreCase("4")) {

                System.out.println("Введите id байка для удаления: ");
                String idInput = scanner.nextLine();
                try {
                    Integer id = Integer.parseInt(idInput);
                    boolean isDeleted = bikeService.delete(id);

                    if (isDeleted) {
                        System.out.println("Байк успешно удален.");
                    } else {
                        System.out.println("Байк с таким ID не найден или не удалось удалить.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка: введены некорректные данные.");
                }

            } else if (scan.equalsIgnoreCase("5")) {
                System.out.println("Введите фильтр для байков в формате: поле условие значение (например, volume > 2):");
                System.out.println("Фильтрация не доступна!!!");
                break;

            } else if (scan.equalsIgnoreCase("6")) {
                break;
            }
        }
    }
}
