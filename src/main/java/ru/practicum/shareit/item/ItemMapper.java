package ru.practicum.shareit.item;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static Item mapToItem(ItemDto itemDto, User user) {
        Item item = Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(user)
                // .request(itemDto.getRequest() != null ? itemRequestStorage.getItemRequest(itemDto.getRequest().getId()) : null)
                .build();
//        return toItemDto(itemStorage.putItem(item));

//        Item item = new Item();
//        item.setOwner(user);
//        item.setUser(user);
//        item.setUrl(itemDto.getUrl());
//        item.setTags(itemDto.getTags());
        return item;
    }

    public static ItemDto mapToItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                // .request(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }




}
