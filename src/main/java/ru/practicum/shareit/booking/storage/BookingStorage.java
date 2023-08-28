package ru.practicum.shareit.booking.storage;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingStorage {

    public Booking putBooking (Booking booking);

    public Booking updateBooking (Booking booking);

    public Booking getBookingById(long bookingId);

    public List<Booking> getAllByBooker (long bookerId, String state);

    public List<Booking> getAllByOwner (long ownerId, String state);
}
