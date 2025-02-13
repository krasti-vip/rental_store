package ru.rental.servic.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

}
