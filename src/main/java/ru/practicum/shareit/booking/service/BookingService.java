package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingService {

    public BookingDtoOut createBooking(BookingDtoIn bookingDtoIn, long userId);

    public BookingDtoOut updateBooking(Boolean approved, long bookingId, long userId);

    public BookingDtoOut getBookingById(long bookingId, long userId);

    public List<Booking> getAllByBooker(long bookerId, String state);

    public List<Booking> getAllByOwner(long ownerId, String state);
}
