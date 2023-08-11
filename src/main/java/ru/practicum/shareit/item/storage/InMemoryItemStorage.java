package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.storage.ItemRequestStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.*;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    @Autowired
    private ItemRequestStorage itemRequestStorage;

    private final Map<Integer, Item> items = new HashMap<>();

    private int itemId = 1;

    private int getNextId() {
        return itemId++;
    }

    @Override
    public ItemDto createItemDto(ItemDto itemDto, int userId) {

        itemDto.setId(getNextId());

        Item item = new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                itemDto.getRequest() != null ? itemRequestStorage.getItemRequest(itemDto.getRequest()) : null
        );
        items.put(item.getId(), item);
        return toItemDto(item);
    }

    @Override
    public ItemDto updateItemDto(ItemDto itemDto, int itemId, int userId) {

        Item oldItem = items.get(itemId);

        if (oldItem.getOwner() == userId) {

            if (itemDto.getName() != null) {
                oldItem.setName(itemDto.getName());
            }

            if (itemDto.getDescription() != null) {
                oldItem.setDescription(itemDto.getDescription());
            }

            if (itemDto.getAvailable() != null) {
                oldItem.setAvailable(itemDto.getAvailable());
            }

            Set<ConstraintViolation<Item>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(oldItem);
            if (!violations.isEmpty()) {
                throw new ValidationException("Данные вещи не прошли валидацию");
            }

            items.put(itemId, oldItem);
            return toItemDto(oldItem);

        } else {
            throw new NotFoundException("Неверно указан владелец вещи");
        }
    }


    @Override
    public ItemDto getItemDtoId(int itemId, int userId) {

        if (!items.containsKey(itemId)) {
            log.warn("Предмета с Id = " + itemId + " нет");
            throw new NotFoundException("Предмета с Id = " + itemId + " нет");
        }

        Item item = items.get(itemId);
        return toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsDtoUserId(int userId) {
        List<ItemDto> itemsDto = new ArrayList<>();
        List<Item> items1 = new ArrayList<>(items.values());
        for (Item item : items1) {
            if (item.getOwner() == userId) {
                itemsDto.add(toItemDto(item));
            }
        }
        return itemsDto;
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        System.out.println("Печать запроса " + text);
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
