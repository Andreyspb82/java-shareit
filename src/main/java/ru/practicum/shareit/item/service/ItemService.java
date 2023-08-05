package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


public interface ItemService {
    public ItemDto createItemDto(ItemDto itemDto, int userId);

    public ItemDto updateItemDto(ItemDto itemDto, int itemId, int userId);

    public ItemDto getItemDtoId(int itemId, int userId);

    public List<ItemDto> getItemsDtoUserId(int userId);

    public List<ItemDto> getItemsBySearch(String text);

}
