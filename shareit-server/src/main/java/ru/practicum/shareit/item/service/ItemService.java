package ru.practicum.shareit.item.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;


public interface ItemService {
    public ItemDto createItem(ItemDto itemDto, long userId);

    public ItemDto updateItem(ItemDto itemDto, long userId);

    public ItemDtoBooking getItemWithBookingById(long itemId, long userId);

    public List<ItemDtoBooking> getItemsByUserId(long userId, Pageable page);

    public List<ItemDto> getItemsByQuery(String query, Pageable page);

    public CommentDto createComment(Comment comment, long itemId, long bookerId);

    public Item getItemById(long itemId);
}
