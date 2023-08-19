package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {
    public ItemDto createItem(ItemDto itemDto, int userId);

    public ItemDto updateItem(ItemDto itemDto, int userId);

    public ItemDto getItemById(int itemId, int userId);

    public List<ItemDto> getItemsByUserId(int userId);

    public List<ItemDto> getItemsByQuery(String query);

}
