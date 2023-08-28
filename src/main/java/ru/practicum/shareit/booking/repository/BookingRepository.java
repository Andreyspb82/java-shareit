package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

//        @Query("select new ru.practicum.shareit.booking.model.Booking ( * from Booking as bo " +
//            "where  bo.booker_id = ?1  " )
    //"order by bo.start desc ) " )
//    @Query (value = "select * from bookings" +
//            "where booker_id = ?1" +
//            "order by start_date desc", nativeQuery = true)
//    List<Booking> getAllByBooker(long bookerId);


    // List<Booking> findByBookerIdOrderByStartDateDesc (long bookerId);

    List<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime nowTime);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime nowTime);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime nowStart, LocalDateTime nowEnd);


    @Query(value = "select * from bookings b where b.booker_id = ?1 and b.status = ?2  order by b.start_date desc", nativeQuery = true)
    List<Booking> findAllByBookerIdAndStatus(long userId, String state);
                //findAllByBookerIdAndStatusOrderByStartDesc
    @Query(value = "select * from bookings b " +
            "where b.item_id in " +
            "(select i.id from items i " +
            "where i.owner_id =?1) " +
            "order by b.start_date desc", nativeQuery = true)
    List <Booking> findAllByOwnerId(long ownerId);

    @Query(value = "select * from bookings b " +
            "where b.item_id in " +
            "(select i.id from items i " +
            "where i.owner_id =?1) " +
            "and b.start_date > ?2 order by b.start_date desc", nativeQuery = true)
    List <Booking> findAllByOwnerIdFuture(long ownerId, LocalDateTime nowTime);

    @Query(value = "select * from bookings b where b.item_id in (select i.id from items i where i.owner_id =?1) and b.end_date < ?2 order by b.start_date desc", nativeQuery = true)
    List <Booking> findAllByOwnerIdPast(long ownerId, LocalDateTime nowTime);

    @Query(value = "select * from bookings b where b.item_id in (select i.id from items i where i.owner_id =?1) and b.start_date < ?2 and b.end_date >?2 order by b.start_date desc", nativeQuery = true)
    List <Booking> findAllByOwnerIdCurrent(long ownerId, LocalDateTime nowTime);

    @Query(value = "select * from bookings b where b.item_id in (select i.id from items i where i.owner_id =?1) and b.status = ?2 order by b.start_date desc", nativeQuery = true)
    List <Booking> findAllByOwnerIdState(long ownerId, String state);

    List <Booking> findAllByItemIdOrderByStartDesc(long itemId); // для добавления бронирования к вещи, не нужен

    //@Query(value = "select * from bookings b where b.item_id=?1  and b.end_date < ?2 order by b.end_date desc limit 1", nativeQuery = true)
    @Query(value = "select * from bookings b where b.item_id=?1  and b.start_date < ?2 order by b.end_date desc limit 1", nativeQuery = true)
    Booking findByItemIdPast(long itemId, LocalDateTime nowTime);


    @Query(value = "select * from bookings b where b.item_id=?1  and b.start_date > ?2 order by b.start_date limit 1", nativeQuery = true)
    Booking findByItemIdFuture(long itemId, LocalDateTime nowTime);

    @Query(value = "select * from bookings b where b.item_id = ?1 and b.booker_id = ?2 and b.end_date < ?3 order by b.end_date asc limit 1", nativeQuery = true)
    Booking findBookingForComment (long itemId, long bookerId, LocalDateTime nowTime);


}
