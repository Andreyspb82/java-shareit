package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    public ItemDto putItem(Item item);

    public ItemDto updateItem(Item item);

    public ItemDto getItemDtoById(int itemId, int userId);

    public Item getItemById(int itemId);

    public List<ItemDto> getItemsDtoByUserId(int userId);

    public List<ItemDto> getItemsByQuery(String text);
}
