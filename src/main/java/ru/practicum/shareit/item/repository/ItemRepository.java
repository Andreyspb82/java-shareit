package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {


//    @Modifying
//    @Transactional
//    @Query("update Item i set i.name = ?1, i.description = ?2, " +
//            "i.available=?3, i.owner=?4 where i.id = ?5")
//    default void updateItem(Item item) {
//
//
//    };  List <Booking> findAllByItemIdOrderByStartDesc(long itemId);

    List<Item> findAllByOrderByIdAsc();

    List <Item> findAllByOwnerIdOrderByIdAsc(long ownerId);

                 //"select * from bookings b where b.item_id = ?1 and b.booker_id = ?2 order by b.end_date asc limit 1"
 //   @Query(value = "select * from bookings b where b.item_id = ?1 and b.booker_id = ?2 order by b.end_date asc limit 1", nativeQuery = true)
 //   Item findAllItemForComment (long itemId, long bookerId);

   // List <Item> findAllOrderById (Sort sort);






}
