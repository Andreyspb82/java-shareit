package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {


    private static final String USER_ID_HEADER = "X-Sharer-User-Id";






}
