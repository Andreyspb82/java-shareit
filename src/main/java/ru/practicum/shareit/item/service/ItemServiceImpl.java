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
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

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

    private final ItemRepository itemRepository;

    private final UserService userService;

    private final BookingRepository bookingRepository;

    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        User user = userService.getUserById(userId);
        Item item = ItemMapper.mapToItem(itemDto, user);

        Set<ConstraintViolation<Item>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(item);
        if (!violations.isEmpty()) {
            throw new ValidationException("Item has not been validated");
        }
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId) {
        User user = userService.getUserById(userId);
        Item oldItem = getItemById(itemDto.getId());

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
        oldItem.setOwner(user);

        Set<ConstraintViolation<Item>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(oldItem);
        if (!violations.isEmpty()) {
            throw new ValidationException("Item has not been validated");
        }
        return ItemMapper.mapToItemDto(itemRepository.save(oldItem));
    }

    @Override
    public ItemDtoBooking getItemById(long itemId, long userId) {
        userService.getUserById(userId);
        Item item = getItemById(itemId);
        List<Comment> comments = commentRepository.findByItemId(itemId);
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
        List<Item> items = itemRepository.findByOwnerIdOrderByIdAsc(userId);
        List<ItemDtoBooking> result = new ArrayList<>();

        for (Item item : items) {
            result.add(getItemById(item.getId(), userId));
        }
        return result;
    }

    @Override
    public List<ItemDto> getItemsByQuery(String query) {
        List<Item> items = itemRepository.search(query);
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
        Item item = getItemById(itemId);
        User user = userService.getUserById(bookerId);
        comment.setItem(item);
        comment.setAuthor(user);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public Item getItemById(long itemId) {
        if (!itemRepository.existsById(itemId)) {
            throw new NotFoundException("Item with Id = " + itemId + " does not exist");
        }
        return itemRepository.findById(itemId).get();
    }
}
