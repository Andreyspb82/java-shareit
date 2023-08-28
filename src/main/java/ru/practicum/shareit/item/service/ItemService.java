package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;


public interface ItemService {
    public ItemDto createItem(ItemDto itemDto, long userId);

    public ItemDto updateItem(ItemDto itemDto, long userId);

    public ItemDto getItemById(long itemId, long userId);

    public ItemDtoBooking getItemByIdTest(long itemId, long userId); // добавил

    public List<ItemDto> getItemsByUserId(long userId);

    public List<ItemDtoBooking> getItemsByUserIdTest(long userId);

    public List<ItemDto> getItemsByQuery(String query);

    public CommentDto createComment (Comment comment, long itemId, long bookerId);

}
