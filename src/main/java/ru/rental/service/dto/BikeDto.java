package ru.rental.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class BikeDto {

    private int id;

    private String name;

    private double price;

    private int horsePower;

    private double volume;

    private Integer userId;
}
