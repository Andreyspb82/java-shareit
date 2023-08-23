package ru.practicum.shareit.booking.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemDto;


@Service
@Slf4j
@Data
public class BookingServiceImpl implements BookingService {

    @Override
    public ItemDto createItem(BookingDto bookingDto, long userId){
        return null;
    }

}
