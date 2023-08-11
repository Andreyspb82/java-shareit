package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    public final ItemService itemService;

    @PostMapping
    public ItemDto createItemDto(@Valid @RequestBody ItemDto itemDto,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.createItemDto(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemDto(@RequestBody ItemDto itemDto,
                                 @PathVariable int itemId,
                                 @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.updateItemDto(itemDto, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemDtoId(@PathVariable int itemId,
                                @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemDtoId(itemId, userId);
    }

    @GetMapping
    public List<ItemDto> getItemsDtoUserId(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemsDtoUserId(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsBySearch(@RequestParam String text) {
        return itemService.getItemsBySearch(text);
    }


}
