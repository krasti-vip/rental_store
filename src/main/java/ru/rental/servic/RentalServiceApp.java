package ru.rental.servic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "ru.rental.servic")
public class RentalServiceApp {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(RentalServiceApp.class);
    }
}
