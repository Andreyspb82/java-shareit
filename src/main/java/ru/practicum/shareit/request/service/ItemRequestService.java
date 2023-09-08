package ru.practicum.shareit.request.service;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;

import java.util.List;

public interface ItemRequestService {

    public ItemRequestDto createItemRequest (ItemRequestDtoIn itemRequestDtoIn, long userId);

    public List<ItemRequestDto> getAllByRequestorId (long userId);

    public List<ItemRequestDto> getAllRequsetByUserId(long userId, Pageable page);

    public ItemRequestDto getRequestById (long userId, long requestId);
}
