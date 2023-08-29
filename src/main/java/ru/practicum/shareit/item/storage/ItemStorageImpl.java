package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private final ItemRepository itemRepository;

    @Override
    public Item putItem(Item item) {
        Set<ConstraintViolation<Item>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(item);
        if (!violations.isEmpty()) {
            throw new ValidationException("Item has not been validated");
        }
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item item) {
        Set<ConstraintViolation<Item>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(item);
        if (!violations.isEmpty()) {
            throw new ValidationException("Item has not been validated");
        }
        return itemRepository.save(item);
    }

    @Override
    public Item getItemById(long itemId) {

        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Item with Id = " + itemId + " does not exist");
        }
        return item.get();
    }

    @Override
    public List<Item> getItems(long ownerId) {
        return itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId);
    }

    @Override
    public List<Item> search(String query) {
        return itemRepository.search(query);
    }

}
