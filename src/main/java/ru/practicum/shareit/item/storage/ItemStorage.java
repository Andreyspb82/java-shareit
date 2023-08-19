package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    public Item putItem(Item item);

    public Item updateItem(Item item);

    public Item getItemById(int itemId);

    public List<Item> getItems();
}
