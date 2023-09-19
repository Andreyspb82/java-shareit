package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
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
    public ItemDtoBooking getItemById(@PathVariable long itemId,
                                      @RequestHeader(USER_ID_HEADER) long userId) {
        return itemService.getItemWithBookingById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoBooking> getItemsByUserId(@RequestHeader(USER_ID_HEADER) long userId,
                                                 @Min(0) @RequestParam(defaultValue = "0") int from,
                                                 @Min(0) @RequestParam(defaultValue = "10") int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return itemService.getItemsByUserId(userId, page);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemsByQuery(@RequestParam("text") String query,
                                         @Min(0) @RequestParam(defaultValue = "0") int from,
                                         @Min(0) @RequestParam(defaultValue = "10") int size) {
        PageRequest page = PageRequest.of(from / size, size);
        return itemService.getItemsByQuery(query, page);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@Valid @RequestBody Comment comment,
                                    @PathVariable("itemId") long itemId,
                                    @RequestHeader(USER_ID_HEADER) long bookerId) {
        return itemService.createComment(comment, itemId, bookerId);
    }
}
