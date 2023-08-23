package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    public final ItemService itemService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";


    @PostMapping
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @PathVariable long itemId,
                              @RequestHeader(USER_ID_HEADER) long userId) {

        itemDto.setId(itemId);
        return itemService.updateItem(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId,
                               @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsByUserId(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestParam("text") String query) {
        return itemService.getItemsByQuery(query);
    }


//    @PostMapping
//    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
//                              @RequestHeader(USER_ID_HEADER) int userId) {
//        return itemService.createItem(itemDto, userId);
//    }
//
//    @PatchMapping("/{itemId}")
//    public ItemDto updateItem(@RequestBody ItemDto itemDto,
//                              @PathVariable int itemId,
//                              @RequestHeader(USER_ID_HEADER) int userId) {
//
//        itemDto.setId(itemId);
//        return itemService.updateItem(itemDto, userId);
//    }
//
//    @GetMapping("/{itemId}")
//    public ItemDto getItemById(@PathVariable int itemId,
//                               @RequestHeader(USER_ID_HEADER) int userId) {
//        return itemService.getItemById(itemId, userId);
//    }
//
//    @GetMapping
//    public List<ItemDto> getItemsByUserId(@RequestHeader(USER_ID_HEADER) int userId) {
//        return itemService.getItemsByUserId(userId);
//    }
//
//    @GetMapping("/search")
//    public List<ItemDto> getItemsByQuery(@RequestParam("text") String query) {
//        return itemService.getItemsByQuery(query);
//    }

}
