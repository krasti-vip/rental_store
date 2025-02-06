package ru.rental.servic.model;

public class Cat {

    public String say() {
        System.out.println("Init method");
        return "Mya";
    }

    public String say2() {
        System.out.println("destroy");
        return "Mya Mya";
    }
}
