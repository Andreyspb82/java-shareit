package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;

public interface BookingStorage {

    public Booking putBooking (Booking booking);

    public Booking updateBooking (Booking booking);

    public Booking getBookingById(long bookingId);
}
