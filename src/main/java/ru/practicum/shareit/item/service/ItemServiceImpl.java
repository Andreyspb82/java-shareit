package ru.practicum.shareit.item.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@Data
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    @Override
    public ItemDto createItemDto(ItemDto itemDto, int userId) {
        userStorage.getUserId(userId);
        return itemStorage.createItemDto(itemDto, userId);

    }

    @Override
    public ItemDto updateItemDto(ItemDto itemDto, int itemId, int userId) {
        userStorage.getUserId(userId);
        return itemStorage.updateItemDto(itemDto, itemId, userId);

       // return null;
    }

    @Override
    public ItemDto getItemDtoId(int itemId, int userId) {
        userStorage.getUserId(userId);
        return itemStorage.getItemDtoId(itemId, userId);
    }


    @Override public List<ItemDto> getItemsDtoUserId(int userId){
        userStorage.getUserId(userId);
        return itemStorage.getItemsDtoUserId(userId);
    }

    @Override
    public List<ItemDto> getItemsBySearch(String text) {
        return itemStorage.getItemsBySearch(text);
    }


}
