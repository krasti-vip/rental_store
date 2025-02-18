package ru.rental.service.crudApp;

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

    protected Scanner getScanner() {
        return scanner;
    }

    public void crud() {
        while (true) {
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
                    addNew();
                    break;
                case "4":
                    delete();
                    break;
                case "5":
                    filter();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Некорректный выбор.");
            }
        }
    }

    public void viewById() {
        System.out.println("Введите id: ");
        String scan = scanner.nextLine();

        try {
            int id = Integer.parseInt(scan);
            var entity = service.get(id);
            entity.ifPresentOrElse(
                    System.out::println,
                    () -> System.out.println("Объект с таким ID не найден.")
            );
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    public void update() {
        System.out.println("Введите id для обновления: ");
        String idInput = scanner.nextLine();
        try {
            int id = Integer.parseInt(idInput);
            Optional<T> maybeEntity = service.get(id);

            if (maybeEntity.isPresent()) {
                T entityToUpdate = maybeEntity.get();
                T updatedEntity = getUpdatedEntityInfo(entityToUpdate);
                Optional<T> result = service.update(id, updatedEntity);

                result.ifPresentOrElse(
                        entity -> System.out.println("Объект успешно обновлен: " + entity),
                        () -> System.out.println("Не удалось обновить объект.")
                );
            } else {
                System.out.println("Объект с таким ID не найден.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    public void addNew() {
        T newEntity = getNewEntityInfo();
        T savedEntity = service.save(newEntity);

        if (savedEntity != null) {
            System.out.println("Новый объект успешно сохранен: " + savedEntity);
        } else {
            System.out.println("Не удалось сохранить объект.");
        }
    }

    public void delete() {
        System.out.println("Введите id для удаления: ");
        String idInput = scanner.nextLine();
        try {
            int id = Integer.parseInt(idInput);
            boolean isDeleted = service.delete(id);

            if (isDeleted) {
                System.out.println("Объект успешно удален.");
            } else {
                System.out.println("Объект с таким ID не найден или не удалось удалить.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: введены некорректные данные.");
        }
    }

    public void filter() {
        System.out.println("Введите фильтр для объектов в формате: поле условие значение (например, volume > 2):");
        System.out.println("Фильтрация не доступна!!!");
    }

    protected abstract T getUpdatedEntityInfo(T entityToUpdate);
    protected abstract T getNewEntityInfo();
}

