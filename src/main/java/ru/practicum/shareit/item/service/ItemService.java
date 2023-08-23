package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {
    public ItemDto createItem(ItemDto itemDto, long userId);

    public ItemDto updateItem(ItemDto itemDto, long userId);

    public ItemDto getItemById(long itemId, long userId);

    public List<ItemDto> getItemsByUserId(long userId);

    public List<ItemDto> getItemsByQuery(String query);

}
