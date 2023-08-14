package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private final Map<Integer, Item> items = new HashMap<>();

    @Override
    public ItemDto putItem(Item item) {
        items.put(item.getId(), item);
        return toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Item item) {
        items.put(item.getId(), item);
        return toItemDto(item);
    }


    @Override
    public ItemDto getItemDtoById(int itemId, int userId) {

        if (!items.containsKey(itemId)) {
            throw new NotFoundException("Предмета с Id = " + itemId + " нет");
        }

        Item item = items.get(itemId);
        return toItemDto(item);
    }

    @Override
    public Item getItemById(int itemId) {
        return items.get(itemId);
    }

    @Override
    public List<ItemDto> getItemsDtoByUserId(int userId) {

        List<Item> items1 = new ArrayList<>(items.values());

        return items1
                .stream()
                .filter(item -> item.getOwner().equals(userId))
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByQuery(String text) {
        List<ItemDto> itemsDto = new ArrayList<>();
        List<Item> items1 = new ArrayList<>(items.values());

        if (text.isEmpty()) {
            return new ArrayList<>();
        }

        for (Item item : items1) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                    item.getAvailable()) {
                itemsDto.add(toItemDto(item));
            }
        }
        return itemsDto;
    }

    private ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

}
