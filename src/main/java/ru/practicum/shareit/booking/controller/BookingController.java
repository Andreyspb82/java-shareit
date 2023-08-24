package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    public final BookingService bookingService;


    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public BookingDtoOut createBooking(@Valid @RequestBody BookingDtoIn bookingDto,
                                 @RequestHeader(USER_ID_HEADER) long userId) {
      //  System.out.println("Печать для проверки " + userId);
        return bookingService.createBooking(bookingDto, userId);
    }



    @PatchMapping("/{bookingId}")
    public BookingDtoOut updateBooking(@RequestParam (name = "approved") @NotNull  Boolean approved,
                                 @PathVariable long bookingId,

                                 @RequestHeader(USER_ID_HEADER) long userId) {
//    public Booking updateBooking(@PathVariable long bookingId,
//                                 @RequestParam(name = "approved") Boolean approved,
//                                 @RequestHeader(USER_ID_HEADER) long userId) {

        // return null;
        System.out.println("Печать для статуса контроллер " + approved);
        return bookingService.updateBooking(approved, bookingId, userId);
    }



    @GetMapping("/{bookingId}")
    public BookingDtoOut getBookingById(@PathVariable long bookingId,
                                  @RequestHeader(USER_ID_HEADER) long userId) {
        return bookingService.getBookingById(bookingId, userId);
        // return bookingService.getItemById(itemId, userId);
    }


}
