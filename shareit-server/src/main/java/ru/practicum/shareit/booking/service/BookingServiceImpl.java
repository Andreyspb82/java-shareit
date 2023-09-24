package ru.practicum.shareit.booking.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.State;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;


@Service
@Slf4j
@Data
public class BookingServiceImpl implements BookingService {

    private final ItemService itemService;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    @Override
    public BookingDtoOut createBooking(BookingDtoIn bookingDtoIn, long userId) {
        User user = userService.getUserById(userId);
        Item item = itemService.getItemById(bookingDtoIn.getItemId());

        if (item.getOwner().getId() == userId) {
            throw new NotFoundException("Item is booked by the owner");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Booking not available");
        }
        if (bookingDtoIn.getStart().isAfter(bookingDtoIn.getEnd())) {
            throw new ValidationException("Booking start is after end");
        }
        if (bookingDtoIn.getStart().equals(bookingDtoIn.getEnd())) {
            throw new ValidationException("Booking start is equal end");
        }

        Booking booking = Booking.builder()
                .start(bookingDtoIn.getStart())
                .end(bookingDtoIn.getEnd())
                .booker(user)
                .item(item)
                .status(Status.WAITING)
                .build();

        return BookingMapper.mapToBookingDtoOut(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOut updateBooking(Boolean approved, long bookingId, long userId) {
        Booking booking = getBookingById(bookingId);
        Item item = booking.getItem();
        User user = item.getOwner();

        if (user.getId() != userId) {
            throw new NotFoundException("Invalid owner");
        }
        if (approved && booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Repeated approval");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
        return BookingMapper.mapToBookingDtoOut(bookingRepository.save(booking));
    }

    @Override
    public BookingDtoOut getBookingById(long bookingId, long userId) {
        userService.getUserById(userId);
        Booking booking = getBookingById(bookingId);
        User booker = booking.getBooker();
        Item item = booking.getItem();
        User owner = item.getOwner();

        if (booker.getId() != userId && owner.getId() != userId) {
            throw new NotFoundException("Invalid userId");
        }
        return BookingMapper.mapToBookingDtoOut(booking);
    }

    @Override
    public List<BookingDtoOut> getAllByBooker(long bookerId, String state, Pageable page) {
        userService.getUserById(bookerId);
        if (state.equals(String.valueOf(State.ALL)) || state.isEmpty()) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository
                    .findByBookerIdOrderByStartDesc(bookerId, page));
        } else if (state.equals(String.valueOf(State.FUTURE))) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository
                    .findByBookerIdAndStartAfterOrderByStartDesc(bookerId, LocalDateTime.now(), page));
        } else if (state.equals(String.valueOf(State.PAST))) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository
                    .findByBookerIdAndEndBeforeOrderByStartDesc(bookerId, LocalDateTime.now(), page));
        } else if (state.equals(String.valueOf(State.CURRENT))) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository
                    .findByBookerIdAndStartBeforeAndEndAfterOrderById(bookerId, LocalDateTime.now(), LocalDateTime.now(), page));
        } else if (state.equals(String.valueOf(State.WAITING)) || state.equals(String.valueOf(State.REJECTED))) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository
                    .findByBookerIdAndStatus(bookerId, state, page));
        } else {
            throw new ValidationException("Unknown state: " + state);
        }
    }

    @Override
    public List<BookingDtoOut> getAllByOwner(long ownerId, String state, Pageable page) {
        userService.getUserById(ownerId);
        if (state.equals(String.valueOf(State.ALL)) || state.isEmpty()) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository.findByOwnerId(ownerId, page));
        } else if (state.equals(String.valueOf(State.FUTURE))) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository.findByOwnerIdFuture(ownerId, page));
        } else if (state.equals(String.valueOf(State.PAST))) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository.findByOwnerIdPast(ownerId, page));
        } else if (state.equals(String.valueOf(State.CURRENT))) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository.findByOwnerIdCurrent(ownerId, page));
        } else if (state.equals(String.valueOf(State.WAITING)) || state.equals(String.valueOf(State.REJECTED))) {
            return BookingMapper.mapToBookingsDtoOut(bookingRepository.findByOwnerIdState(ownerId, state, page));
        } else {
            throw new ValidationException("Unknown state: " + state);
        }
    }

    private Booking getBookingById(long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new NotFoundException("Booking with Id = " + bookingId + " does not exist");
        }
        return bookingRepository.findById(bookingId).get();
    }
}
