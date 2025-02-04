package ru.rental.service.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Bike {

    private int id;

    private String name;

    private double price;

    private int horsePower;

    private double volume;

    private Integer userId;
}
