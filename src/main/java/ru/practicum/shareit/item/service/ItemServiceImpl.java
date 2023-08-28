package ru.practicum.shareit.item.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Data
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    private final BookingRepository bookingRepository;

    private final CommentStorage commentStorage;


    //private final ItemRequestStorage itemRequestStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User user = userStorage.getUserById(userId);
        Item item = ItemMapper.mapToItem(itemDto, user);
        //itemStorage.putItem(item);
        Item item1 = itemStorage.putItem(item);
        // return ItemMapper.mapToItemDto(itemStorage.putItem(item));
        return ItemMapper.mapToItemDto(item1);

//        Item item = Item.builder()
//                .name(itemDto.getName())
//                .description(itemDto.getDescription())
//                .available(itemDto.getAvailable())
//                .owner(userStorage.getUserById(userId))
//               // .request(itemDto.getRequest() != null ? itemRequestStorage.getItemRequest(itemDto.getRequest().getId()) : null)
//                .build();
//        return toItemDto(itemStorage.putItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        User user = userStorage.getUserById(userId);

        Item oldItem = itemStorage.getItemById(itemDto.getId());

        if (oldItem.getOwner().getId() != userId) {
            throw new NotFoundException("Invalid item owner");
        }

        if (itemDto.getName() != null) {
            oldItem.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            oldItem.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            oldItem.setAvailable(itemDto.getAvailable());
        }

        Set<ConstraintViolation<Item>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(oldItem);
        if (!violations.isEmpty()) {
            throw new ValidationException("Item has not been validated");
        }
        oldItem.setOwner(user);

        // Item item = ItemMapper.mapToItem(itemDto, user);

        itemStorage.updateItem(oldItem);
        return ItemMapper.mapToItemDto(oldItem);
    }

//    @Override
//    public ItemDto getItemById(long itemId, long userId) {
//        userStorage.getUserById(userId);
//        return toItemDto(itemStorage.getItemById(itemId));
//    }

    @Override //старый метод, удалить
    public ItemDto getItemById(long itemId, long userId) {

        userStorage.getUserById(userId);
        return toItemDto(itemStorage.getItemById(itemId));
    }

    @Override // add
    public ItemDtoBooking getItemByIdTest(long itemId, long userId) {
        User user = userStorage.getUserById(userId);
        Item item = itemStorage.getItemById(itemId);
        List<Comment> comments = commentStorage.getCommentsByItemId(itemId);
        List<CommentDto> commentsDto = CommentMapper.mapToCommentsDto(comments);
        item.setComments(commentsDto);

        Optional<Booking> bookingPast = Optional.ofNullable(bookingRepository.findByItemIdPast(itemId, LocalDateTime.now()));
        Optional<Booking> bookingFuture = Optional.ofNullable(bookingRepository.findByItemIdFuture(itemId, LocalDateTime.now()));

        if (userId == item.getOwner().getId()) {
            return ItemMapper.mapToItemDtoBookingOwner(bookingPast, bookingFuture, item);
        } else {
            return ItemMapper.mapToItemDtoBookingOwner(Optional.empty(), Optional.empty(), item);
        }

    }


    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        userStorage.getUserById(userId);

        return itemStorage.getItems()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .map(this::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDtoBooking> getItemsByUserIdTest(long userId) {
        List <Item> items = itemStorage.getItemsTest(userId);
        List <ItemDtoBooking> result = new ArrayList<>();

        for (Item item : items) {
            result.add(getItemByIdTest(item.getId(), userId));
        }
        return result;
    }


//    @Override
//    public List<ItemDto> getItemsByUserId(long userId) {
//        userStorage.getUserById(userId);
//
//        return itemStorage.getItems()
//                .stream()
//                .filter(item -> item.getOwner().getId().equals(userId))
//                .map(this::toItemDto)
//                .collect(Collectors.toList());
//    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        List<ItemDto> itemsDto = new ArrayList<>();
        List<Item> itemsList = itemStorage.getItems();

        if (query.isEmpty()) {
            return new ArrayList<>();
        }

        for (Item item : itemsList) {
            if ((item.getName().toLowerCase().contains(query.toLowerCase()) ||
                    item.getDescription().toLowerCase().contains(query.toLowerCase())) &&
                    item.getAvailable()) {
                itemsDto.add(toItemDto(item));
            }
        }
        return itemsDto;
    }


    @Override
    public CommentDto createComment (Comment comment, long itemId, long bookerId){

        Optional<Booking> booking = Optional.ofNullable(bookingRepository.findBookingForComment(itemId, bookerId, LocalDateTime.now()));

        if (booking.isEmpty()) {
            throw new ValidationException("Test ");
        }

        Item item = itemStorage.getItemById(itemId);
        User user = userStorage.getUserById(bookerId);

        comment.setItem(item);
        comment.setAuthor(user);

        Comment comment1 = commentStorage.putComment(comment);

        return CommentMapper.mapToCommentDto(comment1);



    }

    private ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                // .request(item.getRequest() != null ? item.getRequest() : null)
                .build();
    }

}
