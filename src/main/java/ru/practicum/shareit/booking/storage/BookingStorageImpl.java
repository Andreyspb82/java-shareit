package ru.practicum.shareit.booking.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookingStorageImpl implements BookingStorage {

    private final BookingRepository bookingRepository;

    private LocalDateTime nowTime = LocalDateTime.now();

    @Override
    public Booking putBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }


    @Override
    public Booking getBookingById(long bookingId) {

        Optional<Booking> booking = bookingRepository.findById(bookingId);
        if (booking.isEmpty()) {
            throw new NotFoundException("Booking with Id = " + bookingId + " does not exist");
        }
        return booking.get();
    }

    @Override
    public List<Booking> getAllByBooker(long bookerId, String state) {

        if (state.equals(String.valueOf(State.ALL)) || state.isEmpty()) {
            return bookingRepository.findAllByBookerIdOrderByStartDesc(bookerId);
        } else if (state.equals(String.valueOf(State.FUTURE))) {
            return bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now());
        } else if (state.equals(String.valueOf(State.PAST))) {
            return bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now());
        } else if (state.equals(String.valueOf(State.CURRENT))) {
            return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(bookerId, LocalDateTime.now(), LocalDateTime.now());
        } else if (state.equals(String.valueOf(State.WAITING)) || state.equals(String.valueOf(State.REJECTED))) {
            return bookingRepository.findAllByBookerIdAndStatus(bookerId, state);
        } else {
            throw new ValidationException("Unknown state: " + state);
        }

    }

    @Override
    public List<Booking> getAllByOwner(long ownerId, String state) {
        System.out.println("Печать из стораже " + state);
        if (state.equals(String.valueOf(State.ALL)) || state.isEmpty()) {
            return bookingRepository.findAllByOwnerId(ownerId);
        } else if (state.equals(String.valueOf(State.FUTURE))) {
            return bookingRepository.findAllByOwnerIdFuture(ownerId, LocalDateTime.now());
        } else if (state.equals(String.valueOf(State.PAST))) {
            return bookingRepository.findAllByOwnerIdPast(ownerId, LocalDateTime.now());
        } else if (state.equals(String.valueOf(State.CURRENT))) {
            return bookingRepository.findAllByOwnerIdCurrent(ownerId, LocalDateTime.now());
        } else if (state.equals(String.valueOf(State.WAITING)) || state.equals(String.valueOf(State.REJECTED))) {
            return bookingRepository.findAllByOwnerIdState(ownerId, state);
        } else {
            throw new ValidationException("Unknown state: " + state);
        }
    }


}
