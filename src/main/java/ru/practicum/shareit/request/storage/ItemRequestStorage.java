package ru.practicum.shareit.request.storage;

import ru.practicum.shareit.request.model.ItemRequest;

public interface ItemRequestStorage {

    public ItemRequest getItemRequest(int itemRequestId);
}