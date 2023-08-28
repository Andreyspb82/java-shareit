package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    public final BookingService bookingService;


    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoOut createBooking(@Valid @RequestBody BookingDtoIn bookingDtoIn,
                                       @RequestHeader(USER_ID_HEADER) long userId) {
        //  System.out.println("Печать для проверки " + userId);
        return bookingService.createBooking(bookingDtoIn, userId);
    }


    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateBooking(@RequestParam(name = "approved") @NotNull Boolean approved,
                                       @PathVariable long bookingId,
                                       @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.updateBooking(approved, bookingId, userId);
    }


    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@PathVariable long bookingId,
                                        @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping
    public List<Booking> getAllByBooker (@RequestHeader(USER_ID_HEADER) long bookerId,
                                         @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getAllByBooker(bookerId, state);
    }

    @GetMapping("/owner")
    public List<Booking> getAllByOwner (@RequestHeader(USER_ID_HEADER) long ownerId,
                                         @RequestParam(required = false, defaultValue = "ALL") String state) {
        return bookingService.getAllByOwner(ownerId, state);
    }


}
