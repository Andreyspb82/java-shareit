package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;

   // @NotEmpty
    private String name;

   // @Email
   // @NotNull
    private String email;

}
