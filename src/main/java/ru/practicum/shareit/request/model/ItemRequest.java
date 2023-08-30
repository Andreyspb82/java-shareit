package ru.practicum.shareit.request.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class ItemRequest {

    private Integer id;

    private String description;

    private Integer requestor;

    private LocalDateTime created;
}
