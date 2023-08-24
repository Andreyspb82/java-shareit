package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingMapper {

    public static BookingDtoOut mapToBookingDtoOut(Booking booking) {
        Item item = booking.getItem();
       // User user = item.getOwner();
        ItemDto itemDto = ItemMapper.mapToItemDto(item);
       // UserDto userDto = UserMapper.

        BookingDtoOut bookingDtoOut = BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(itemDto)
                .booker(booking.getBooker())
                .status(booking.getStatus())
                .build();





        return bookingDtoOut;

    }
}
