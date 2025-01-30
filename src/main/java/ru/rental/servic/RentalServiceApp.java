package ru.rental.servic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.rental.servic.service.BikeService;

import java.util.Scanner;

@Configuration
@ComponentScan(basePackages = "ru.rental.servic")
public class RentalServiceApp {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(RentalServiceApp.class);
        final var bikeService = context.getBean("bikeService", BikeService.class);
        Scanner scanner = new Scanner(System.in);
        System.out.println(bikeService.getAll());


        // CLI CRUD под все обьекты

        System.out.println("С каким обьектом хочешь взаимодейтсвовать? Байк кар юзер?");
//        if ("bike") {
//            bikeService.getAll();
//        }
        System.out.println("Что хочешь с ним сделать? ");
    }

    // CLI
    // 1. Параметризованные тесты
    // 2. CLI CRUD
    // 3. Подумай как юзеру добавить в базу машины и байки? (Many to many | one to many | many to one | one to one)
    // https://habr.com/ru/articles/488054/
    // https://metanit.com/sql/mysql/2.5.php
}
