package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;


@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@Validated
public class ItemRequestController {

    public final ItemRequestService itemRequestService;

    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ItemRequestDto createItemRequest(@Valid @RequestBody ItemRequestDtoIn itemRequestDtoIn,
                                            @RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestService.createItemRequest(itemRequestDtoIn, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByRequestorId(@RequestHeader(USER_ID_HEADER) long userId) {
        return itemRequestService.getAllByRequestorId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllByRequestsByUserId(@RequestHeader(USER_ID_HEADER) long userId,
                                                         @Min(0) @RequestParam(defaultValue = "0") int from,
                                                         @Min(0) @RequestParam(defaultValue = "10") int size) {
        PageRequest page = PageRequest.of(from > 0 ? from / size : 0, size);
        return itemRequestService.getAllRequsetByUserId(userId, page);
    }

    @GetMapping ("{requestId}")
    public ItemRequestDto getRequestById (@RequestHeader(USER_ID_HEADER) long userId,
                                          @PathVariable long requestId){
        return itemRequestService.getRequestById(userId, requestId);
    }





}
