package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

  //  private static LocalDateTime nowTime = LocalDateTime.now();

    public static Item mapToItem(ItemDto itemDto, User user) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                // .request(itemDto.getRequest() != null ? itemRequestStorage.getItemRequest(itemDto.getRequest().getId()) : null)
                .build();
//        return toItemDto(itemStorage.putItem(item));

//        Item item = new Item();
//        item.setOwner(user);
//        item.setUser(user);
//        item.setUrl(itemDto.getUrl());
//        item.setTags(itemDto.getTags());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                // .request(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }

    public static ItemDtoBooking mapToItemsDtoBooking (List <Booking> bookings, Item item){
        List <ItemDtoBooking> result = new ArrayList<>();
        List <Booking> tempBooking = new ArrayList<>();

//        for (Booking booking : bookings) {
//            if (booking.getEnd().isBefore(nowTime))
//        }

        for (int i = 0; i < bookings.size(); i++) {
            if (bookings.get(i).getEnd().isBefore(LocalDateTime.now()) && bookings.get(i+1).getStart().isAfter(LocalDateTime.now())) {
                tempBooking.add(bookings.get(i));
                tempBooking.add(bookings.get(i+1));
            }
        }

        return   ItemDtoBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .lastBooking(BookingMapper.mapToBookingDtoForItem(tempBooking.get(1)))
                .nextBooking(BookingMapper.mapToBookingDtoForItem(tempBooking.get(0)))
                .build();

//        return   ItemDtoBooking.builder()
//                .id(item.getId())
//                .name(item.getName())
//                .description(item.getDescription())
//                .available(item.getAvailable())
//                .lastBooking(BookingMapper.mapToBookingDtoForItem(bookings.get(0)))
//                .nextBooking(BookingMapper.mapToBookingDtoForItem(bookings.get(1)))
//                .build();
    }

    public static ItemDtoBooking mapToItemDtoBookingOwner (Optional<Booking> bookingPast, Optional<Booking> bookingFuture, Item item) {

        if (bookingPast.isPresent() && bookingFuture.isPresent()) {
            return   ItemDtoBooking.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(BookingMapper.mapToBookingDtoForItem(bookingPast.get()))
                    .nextBooking(BookingMapper.mapToBookingDtoForItem(bookingFuture.get()))
                    .comments(item.getComments())
                    .build();


        } if (bookingPast.isEmpty() && bookingFuture.isPresent()) {
            return   ItemDtoBooking.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(null)
                    .nextBooking(null)
                    //.nextBooking(BookingMapper.mapToBookingDtoForItem(bookingFuture.get()))
                    .comments(item.getComments())
                    .build();
        } if (bookingPast.isPresent() && bookingFuture.isEmpty()) {
            return   ItemDtoBooking.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(BookingMapper.mapToBookingDtoForItem(bookingPast.get()))
                    .nextBooking(null)
                    .comments(item.getComments())
                    .build();
        } else {
            return   ItemDtoBooking.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(null)
                    .nextBooking(null)
                    .comments(item.getComments())
                    .build();

        }
    }


    private static ItemDtoBooking mapToItemDtoBookingOwnerTest (Booking bookingPast, Booking bookingFuture, Item item) {
        return   ItemDtoBooking.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .lastBooking(BookingMapper.mapToBookingDtoForItem(bookingPast))
                .nextBooking(BookingMapper.mapToBookingDtoForItem(bookingFuture))
                .build();

    }




}
