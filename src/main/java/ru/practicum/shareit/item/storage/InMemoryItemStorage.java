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
public class InMemoryItemStorage implements ItemStorage {

    private final ItemRepository itemRepository;

//    private final Map<Long, Item> items = new HashMap<>();
//
//    private long itemId = 1;
//
//
//    public long getNextId() {
//        return itemId++;
//    }

    @Override
    public Item putItem(Item item) {
        Set<ConstraintViolation<Item>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(item);
        if (!violations.isEmpty()) {
            throw new ValidationException("Item has not been validated");
        }
        return itemRepository.save(item);


//        item.setId(getNextId());
//        items.put(item.getId(), item);
//        log.info("POST/items request, item added");
//        return item;
    }

    @Override
    public Item updateItem(Item item) {
        Set<ConstraintViolation<Item>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(item);
        if (!violations.isEmpty()) {
            throw new ValidationException("Item has not been validated");
        }
        return itemRepository.save(item);
//        items.put(item.getId(), item);
//        log.info("PATCH/items request, item updated");
//        return item;
        // return null;
    }

    @Override
    public Item getItemById(long itemId) {

        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) {
            throw new NotFoundException("Item with Id = " + itemId + " does not exist");
        }

//        if (!items.containsKey(itemId)) {
//            throw new NotFoundException("Item with Id = " + itemId + " does not exist");
//        }
//        return items.get(itemId);
        return item.get();
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findAllByOrderByIdAsc();


    }

    @Override
    public List<Item> getItemsTest(long ownerId) {

        return itemRepository.findAllByOwnerIdOrderByIdAsc(ownerId);

    }

//    @Override
//    public Item getItemsForComment(long itemId, long bookerId) {
//        Item item8 = itemRepository.findAllItemForComment(itemId, bookerId);
//        System.out.println("Test888 " + item8);
//
//        Optional<Item> item = Optional.ofNullable(itemRepository.findAllItemForComment(itemId, bookerId));
//
//
////        if (item.isEmpty()) {
////            throw new ValidationException("Test ");
////        }
//        System.out.println("Test999 " + item.get());
//
//        return item.get();
//    }

}
