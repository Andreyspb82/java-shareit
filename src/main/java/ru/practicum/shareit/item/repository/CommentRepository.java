package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


   // List<Comment> findAllByItemIdByOrderIdAsc(long itemId);
    //findAllByOwnerIdOrderByIdAsc(long ownerId);

    @Query(value = "select * from comments c where c.item_id = ?1", nativeQuery = true)
    List<Comment> findAllByItemIdB(long itemId);
}
