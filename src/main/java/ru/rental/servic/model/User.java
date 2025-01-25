package ru.rental.servic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
