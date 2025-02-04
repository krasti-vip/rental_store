package ru.rental.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rental.service.model.Bike;
import ru.rental.service.model.Car;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserDto {

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
