package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import javax.transaction.Transactional;

public interface ItemRepository extends JpaRepository<Item, Long> {


//    @Modifying
//    @Transactional
//    @Query("update Item i set i.name = ?1, i.description = ?2, " +
//            "i.available=?3, i.owner=?4 where i.id = ?5")
//    default void updateItem(Item item) {
//
//
//    };
}
