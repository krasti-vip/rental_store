package ru.rental.servic.model;

public class Dog {

    private final Cat cat;

    public Dog(Cat cat) {
        this.cat = cat;
    }

    public void say() {
        System.out.println("init dog");
    }

    public void say2() {
        System.out.println("destroy dog");
    }
}
