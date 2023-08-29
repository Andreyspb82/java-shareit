package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findAllByBookerIdOrderByStartDesc(long userId);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(long userId, LocalDateTime nowTime);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(long userId, LocalDateTime nowTime);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(long userId, LocalDateTime nowStart, LocalDateTime nowEnd);


    @Query(value = "select * from bookings b" +
            " where b.booker_id = ?1 and b.status = ?2  order by b.start_date desc", nativeQuery = true)
    List<Booking> findAllByBookerIdAndStatus(long userId, String state);

    @Query(value = "select * from bookings b " +
            "where b.item_id in " +
            "(select i.id from items i " +
            "where i.owner_id =?1) " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findAllByOwnerId(long ownerId);

    @Query(value = "select * from bookings b " +
            "where b.item_id in " +
            "(select i.id from items i " +
            "where i.owner_id =?1) " +
            "and b.start_date > current_timestamp order by b.start_date desc", nativeQuery = true)
    List<Booking> findAllByOwnerIdFuture(long ownerId);

    @Query(value = "select * from bookings b " +
            "where b.item_id in (select i.id from items i where i.owner_id =?1) " +
            "and b.end_date < current_timestamp " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findAllByOwnerIdPast(long ownerId);

    @Query(value = "select * from bookings b " +
            "where b.item_id in (select i.id from items i where i.owner_id =?1) " +
            "and b.start_date < current_timestamp and b.end_date > current_timestamp " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findAllByOwnerIdCurrent(long ownerId);

    @Query(value = "select * from bookings b " +
            "where b.item_id in (select i.id from items i where i.owner_id =?1) " +
            "and b.status = ?2 " +
            "order by b.start_date desc", nativeQuery = true)
    List<Booking> findAllByOwnerIdState(long ownerId, String state);

    @Query(value = "select * from bookings b " +
            "where b.item_id=?1 and b.start_date < current_timestamp and b.status = 'APPROVED' " +
            "order by b.end_date desc limit 1", nativeQuery = true)
    Booking findByItemIdLast(long itemId);

    @Query(value = "select * from bookings b " +
            "where b.item_id=?1  and b.start_date > current_timestamp and b.status = 'APPROVED' " +
            "order by b.start_date limit 1", nativeQuery = true)
    Booking findByItemIdNext(long itemId);

    @Query(value = "select * from bookings b " +
            "where b.item_id = ?1 and b.booker_id = ?2 and b.end_date < current_timestamp " +
            "order by b.end_date asc limit 1", nativeQuery = true)
    Booking findBookingForComment(long itemId, long bookerId);


}
