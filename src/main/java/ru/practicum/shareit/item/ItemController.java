package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    public final ItemService itemService;

    @PostMapping
    public ItemDto createItemDto(@Valid @RequestBody ItemDto itemDto, @RequestHeader int itemId) {


        return itemService.createItemDto(itemDto, itemId);
        // return userService.createUser(user);
    }


}
