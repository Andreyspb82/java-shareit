package ru.practicum.shareit.booking.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;


@Service
@Slf4j
@Data
public class BookingServiceImpl implements BookingService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    private final BookingStorage bookingStorage;

    @Override
    public BookingDtoOut createBooking(BookingDtoIn bookingDtoIn, long userId){
        User user = userStorage.getUserById(userId);
        Item item = itemStorage.getItemById(bookingDtoIn.getItemId());

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

        Set<ConstraintViolation<Booking>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(booking);
        if (!violations.isEmpty()) {
            throw new ValidationException("Booking has not been validated");
        }

        return BookingMapper.mapToBookingDtoOut(bookingStorage.putBooking(booking));

    }

    @Override
    public BookingDtoOut updateBooking(Boolean approved, long bookingId,  long userId){
       User userTest = userStorage.getUserById(userId);
        Booking booking = bookingStorage.getBookingById(bookingId);
        Item item = booking.getItem();
        User user = item.getOwner();
        System.out.println("Печать для статуса сервис " + approved);

        if (!user.equals(userTest)) {
            throw new ValidationException("Owner not validated");
        }
//        if (userId != user.getId()) {
//            throw new ValidationException("Owner not validated");
//        }

        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }

     // return bookingStorage.updateBooking(booking);

       return BookingMapper.mapToBookingDtoOut(bookingStorage.updateBooking(booking));

      //  return null;
    }

    @Override
    public BookingDtoOut getBookingById(long bookingId, long userId){
        userStorage.getUserById(userId);



        return BookingMapper.mapToBookingDtoOut(bookingStorage.getBookingById(bookingId));
    }

}
