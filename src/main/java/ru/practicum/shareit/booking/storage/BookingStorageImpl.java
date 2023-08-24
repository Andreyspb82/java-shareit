package ru.practicum.shareit.booking.storage;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookingStorageImpl implements BookingStorage {

    private final BookingRepository bookingRepository;

    @Override
    public Booking putBooking (Booking booking){

        return bookingRepository.save(booking);
       // return null;
    }

    @Override
    public Booking updateBooking (Booking booking){
        return bookingRepository.save(booking);
      //  return null;
    }


    @Override
    public Booking getBookingById(long bookingId){

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException("Booking with Id = " + bookingId + " does not exist");
        }
        return booking.get();
    }


}
