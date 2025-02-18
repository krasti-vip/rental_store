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

    @Override
    public String toString() {
        return String.format("UserDto(id=%d,\n" +
                        "        userName=%s,\n" +
                        "        firstName=%s,\n" +
                        "        lastName=%s,\n" +
                        "        passport=%s,\n" +
                        "        email=%s,\n" +
                        "        bankCard=%s,\n" +
                        "        listBike=%s,\n" +
                        "        listCar=%s)",
                id, userName, firstName, lastName, passport, email, bankCard,
                listBike != null ? listBike : "null", listCar != null ? listCar : "null");
    }
}
