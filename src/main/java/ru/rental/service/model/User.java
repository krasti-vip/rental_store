package ru.rental.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {

    private int id;

    private String userName;

    private String firstName;

    private String lastName;

    private int passport;

    private String email;

    private long bankCard;

    private List<Bike> listBike;

    private List<Car> listCar;
}
