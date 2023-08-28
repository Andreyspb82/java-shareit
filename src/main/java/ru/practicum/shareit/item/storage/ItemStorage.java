package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemStorage {

    public Item putItem(Item item);

    public Item updateItem(Item item);

    public Item getItemById(long itemId);

    public List<Item> getItems();

    public List<Item> getItemsTest(long ownerId);

  //  public Item getItemsForComment(long bookerId, long itemId);
}
