package ru.practicum.shareit.item.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    private final ItemRequestStorage itemRequestStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, int userId) {
        userStorage.getUserById(userId);

        itemDto.setId(itemStorage.getNextId());

        Item item = Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(userStorage.getUserById(userId))
                .request(itemDto.getRequest() != null ? itemRequestStorage.getItemRequest(itemDto.getRequest().getId()) : null)
                .build();
        return toItemDto(itemStorage.putItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int userId) {
        userStorage.getUserById(userId);

        Item oldItem = itemStorage.getItemById(itemDto.getId());

        if (oldItem.getOwner().getId() != userId) {
            throw new NotFoundException("Invalid item owner");
        }

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
            throw new ValidationException("Item has not been validated");
        }

        return toItemDto(itemStorage.updateItem(oldItem));
    }

    @Override
    public ItemDto getItemById(int itemId, int userId) {
        userStorage.getUserById(userId);
        return toItemDto(itemStorage.getItemById(itemId));
    }


    @Override
    public List<ItemDto> getItemsByUserId(int userId) {
        userStorage.getUserById(userId);

        return itemStorage.getItems()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        List<ItemDto> itemsDto = new ArrayList<>();
        List<Item> itemsList = itemStorage.getItems();

        if (query.isEmpty()) {
            return new ArrayList<>();
        }

        for (Item item : itemsList) {
            if ((item.getName().toLowerCase().contains(query.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(query.toLowerCase())) &&
                    item.getAvailable()) {
                itemsDto.add(toItemDto(item));
            }
        }
        return itemsDto;
    }

    private ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .request(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }

}
