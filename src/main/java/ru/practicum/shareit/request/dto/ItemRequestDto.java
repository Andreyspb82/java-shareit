package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
public class ItemRequestDto {

    @Positive
    private Integer id;

    @NotBlank
    private String description;

    @Positive
    private Integer requestor;

    @PastOrPresent
    private LocalDateTime created;

}
