package ru.rental.servic.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Car {

    private int id;

    private String name;

    private double price;

    private int horsePower;

    private double volume;

    private String color;
}
