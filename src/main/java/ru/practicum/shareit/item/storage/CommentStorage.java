package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.repository.CommentRepository;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommentStorage {

    private final CommentRepository commentRepository;

    public Comment putComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByItemId(long itemId) {
        return commentRepository.findAllByItemIdB(itemId);
    }
}
