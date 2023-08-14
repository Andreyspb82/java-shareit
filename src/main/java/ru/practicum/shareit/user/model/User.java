package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
public class User {

    private Integer id;

    @NotEmpty
    private String name;

    @Email
    @NotNull
    private String email;
}
