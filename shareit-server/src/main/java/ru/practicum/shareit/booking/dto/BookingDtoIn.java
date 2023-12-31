package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@Builder
public class BookingDtoIn {

    private Long id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Long itemId;

    public BookingDtoIn() {
    }
}
