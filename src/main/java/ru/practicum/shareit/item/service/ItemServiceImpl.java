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
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Data
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    private final ItemRequestStorage itemRequestStorage;

    private int itemId = 1;

    private int getNextId() {
        return itemId++;
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, int userId) {
        userStorage.getUserById(userId);

        itemDto.setId(getNextId());
        Item item = new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userId,
                itemDto.getRequest() != null ? itemRequestStorage.getItemRequest(itemDto.getRequest()) : null
        );

        return itemStorage.putItem(item);

    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, int userId) {
        userStorage.getUserById(userId);

        Item oldItem = itemStorage.getItemById(itemId);

        if (oldItem.getOwner() != userId) {
            throw new NotFoundException("Неверно указан владелец вещи");
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
            throw new ValidationException("Данные вещи не прошли валидацию");
        }

        return itemStorage.updateItem(oldItem);
    }

    @Override
    public ItemDto getItemDtoById(int itemId, int userId) {
        userStorage.getUserById(userId);
        return itemStorage.getItemDtoById(itemId, userId);
    }


    @Override
    public List<ItemDto> getItemsDtoByUserId(int userId) {
        userStorage.getUserById(userId);
        return itemStorage.getItemsDtoByUserId(userId);
    }

    @Override
    public List<ItemDto> getItemsByQuery(String text) {
        return itemStorage.getItemsByQuery(text);
    }


}
