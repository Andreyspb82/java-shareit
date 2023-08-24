package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;

public interface BookingService {

    public BookingDtoOut createBooking(BookingDtoIn bookingDtoIn, long userId);

    public BookingDtoOut updateBooking(Boolean approved, long bookingId, long userId);

    public BookingDtoOut getBookingById(long bookingId, long userId);
}
