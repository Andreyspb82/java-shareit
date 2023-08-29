package ru.practicum.shareit.item.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.CommentStorage;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Slf4j
@Data
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;

    private final UserStorage userStorage;

    private final BookingRepository bookingRepository;

    private final CommentStorage commentStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User user = userStorage.getUserById(userId);
        Item item = itemStorage.putItem(ItemMapper.mapToItem(itemDto, user));
        return ItemMapper.mapToItemDto(item);
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

        return ItemMapper.mapToItemDto(itemStorage.updateItem(oldItem));
    }

    @Override
    public ItemDtoBooking getItemById(long itemId, long userId) {
        userStorage.getUserById(userId);
        Item item = itemStorage.getItemById(itemId);
        List<Comment> comments = commentStorage.getCommentsByItemId(itemId);
        List<CommentDto> commentsDto = CommentMapper.mapToCommentsDto(comments);
        item.setComments(commentsDto);

        Optional<Booking> bookingLast = Optional.ofNullable(bookingRepository.findByItemIdLast(itemId));
        Optional<Booking> bookingNext = Optional.ofNullable(bookingRepository.findByItemIdNext(itemId));

        if (userId == item.getOwner().getId()) {
            return ItemMapper.mapToItemDtoBooking(bookingLast, bookingNext, item);
        } else {
            return ItemMapper.mapToItemDtoBooking(Optional.empty(), Optional.empty(), item);
        }
    }

    @Override
    public List<ItemDtoBooking> getItemsByUserId(long userId) {
        List<Item> items = itemStorage.getItems(userId);
        List<ItemDtoBooking> result = new ArrayList<>();

        for (Item item : items) {
            result.add(getItemById(item.getId(), userId));
        }
        return result;
    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        List<Item> items = itemStorage.search(query);

        if (query.isEmpty()) {
            return new ArrayList<>();
        }
        return ItemMapper.mapToItemsDto(items);
    }

    @Override
    public CommentDto createComment(Comment comment, long itemId, long bookerId) {

        Optional<Booking> booking = Optional.ofNullable(bookingRepository.findBookingForComment(itemId, bookerId));
        if (booking.isEmpty()) {
            throw new ValidationException("The user has not used the item");
        }

        Item item = itemStorage.getItemById(itemId);
        User user = userStorage.getUserById(bookerId);

        comment.setItem(item);
        comment.setAuthor(user);

        return CommentMapper.mapToCommentDto(commentStorage.putComment(comment));
    }

}
