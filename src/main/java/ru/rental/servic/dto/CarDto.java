package ru.rental.servic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CarDto {

    private int id;

    private String title;

    private double price;

    private int horsePower;

    private double volume;

    private String color;
}
