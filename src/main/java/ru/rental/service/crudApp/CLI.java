package ru.rental.service.crudApp;

import ru.rental.service.dto.BikeDto;
import ru.rental.service.service.Service;
import ru.rental.service.util.PropertiesUtil;

import java.util.Optional;
import java.util.Scanner;

public abstract class CLI<T> {

    private static final String CRUD = PropertiesUtil.getPropertyMenu("crud");

    private final Scanner scanner;

    private final Service<T, Integer> service;

    protected CLI(Scanner scanner, Service<T, Integer> service) {
        this.scanner = scanner;
        this.service = service;
    }

    public void crud() {
        while (true) {
            display();
            System.out.println(CRUD);
            String scan = scanner.nextLine();

            switch (scan.toLowerCase()) {
                case "1":
                    viewById();
                    break;
                case "2":
                    update();
                    break;
                case "3":
                    addNewBike();
                    break;
                case "4":
                    deleteBike();
                    break;
                case "5":
                    filterBikes();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Некорректный выбор.");
            }
        }
    }

    private void display() {
        service.getAll().stream().forEach(System.out::println);
    }

    private void viewById() {
        System.out.println("Введите id: ");
        String scan = scanner.nextLine();

        try {
            int id = Integer.parseInt(scan);
            var bike = service.get(id);
            bike.ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Байк с таким ID не найден.")
            );
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    private void update() {
        System.out.println("Введите id байка для обновления: ");
        String idInput = scanner.nextLine();
        try {
            int id = Integer.parseInt(idInput);
            Optional<BikeDto> maybeBike = bikeService.get(id);

            if (maybeBike.isPresent()) {
                BikeDto bikeToUpdate = maybeBike.get();
                BikeDto updatedBike = getUpdatedBikeInfo(bikeToUpdate);
                Optional<BikeDto> result = bikeService.update(id, updatedBike);

                result.ifPresentOrElse(
                        bike -> System.out.println("Байк успешно обновлен: " + bike),
                        () -> System.out.println("Не удалось обновить байк.")
                );
            } else {
                System.out.println("Байк с таким ID не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    private BikeDto getUpdatedBikeInfo(BikeDto bikeToUpdate) {
        System.out.println("Введите новое имя байка (текущее: " + bikeToUpdate.getName() + "):");
        String name = scanner.nextLine();

        System.out.println("Введите новую цену байка (текущая: " + bikeToUpdate.getPrice() + "):");
        int price = Integer.parseInt(scanner.nextLine());

        System.out.println("Введите новый объем байка (текущий: " + bikeToUpdate.getVolume() + "):");
        double volume = Double.parseDouble(scanner.nextLine());

        System.out.println("Введите новую мощность байка (текущая: " + bikeToUpdate.getHorsePower() + "):");
        int horsePower = Integer.parseInt(scanner.nextLine());

        return new BikeDto(bikeToUpdate.getId(), name, price, horsePower, volume, null);
    }

    private void addNewBike() {
        System.out.println("Введите имя нового байка:");
        String name = scanner.nextLine();

        System.out.println("Введите цену нового байка:");
        double price = Double.parseDouble(scanner.nextLine());

        System.out.println("Введите объем нового байка:");
        double volume = Double.parseDouble(scanner.nextLine());

        System.out.println("Введите мощность нового байка:");
        int horsePower = Integer.parseInt(scanner.nextLine());

        BikeDto newBike = new BikeDto(0, name, price, horsePower, volume, null);
        BikeDto savedBike = bikeService.save(newBike);

        if (savedBike != null) {
            System.out.println("Новый байк успешно сохранен: " + savedBike);
        } else {
            System.out.println("Не удалось сохранить байк.");
        }
    }

    private void deleteBike() {
        System.out.println("Введите id байка для удаления: ");
        String idInput = scanner.nextLine();
        try {
            int id = Integer.parseInt(idInput);
            boolean isDeleted = bikeService.delete(id);

            if (isDeleted) {
                System.out.println("Байк успешно удален.");
            } else {
                System.out.println("Байк с таким ID не найден или не удалось удалить.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    private void filterBikes() {
        System.out.println("Введите фильтр для байков в формате: поле условие значение (например, volume > 2):");
        System.out.println("Фильтрация не доступна!!!");
    }
}
