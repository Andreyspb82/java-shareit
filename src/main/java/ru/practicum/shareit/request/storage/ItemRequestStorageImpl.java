package ru.practicum.shareit.request.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.request.model.ItemRequest;

@Component
@Slf4j
public class ItemRequestStorageImpl implements ItemRequestStorage {

    @Override
    public ItemRequest getItemRequest(int itemRequestId) {
        return null;
    }

}
