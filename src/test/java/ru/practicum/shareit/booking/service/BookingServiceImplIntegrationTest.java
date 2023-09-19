package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {"db.name=shareitTest"})
@Transactional
class BookingServiceImplIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private EntityManager em;

    private User userOwner;

    private User userBooker;

    private User userTest;

    private ItemDto itemDto;

    private BookingDtoIn bookingDtoInTest;

    @BeforeEach
    public void setUp() {

        UserDto userDtoOwner = new UserDto();
        userDtoOwner.setName("Name");
        userDtoOwner.setEmail("email@mail.ru");
        userOwner = userService.createUser(userDtoOwner);

        UserDto userDtoBooker = new UserDto();
        userDtoBooker.setName("booker");
        userDtoBooker.setEmail("booker@mail.ru");
        userBooker = userService.createUser(userDtoBooker);

        UserDto userDtoTest = new UserDto();
        userDtoTest.setName("userTest");
        userDtoTest.setEmail("test@mail.ru");
        userTest = userService.createUser(userDtoTest);

        ItemDto itemDtoTest = ItemDto.builder()
                .name("itemDto1")
                .description("descriptionDto1")
                .available(true)
                .build();
        itemDto = itemService.createItem(itemDtoTest, userOwner.getId());

        bookingDtoInTest = new BookingDtoIn();
        bookingDtoInTest.setStart(LocalDateTime.now().plusSeconds(2));
        bookingDtoInTest.setEnd(LocalDateTime.now().plusSeconds(3));
        bookingDtoInTest.setItemId(itemDto.getId());
    }

    @Test
    public void createBooking() {
        bookingService.createBooking(bookingDtoInTest, userBooker.getId());

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.status = :status", Booking.class);
        Booking booking = query
                .setParameter("status", Status.WAITING)
                .getSingleResult();

        assertEquals(bookingDtoInTest.getStart(), booking.getStart());
        assertEquals(bookingDtoInTest.getEnd(), booking.getEnd());
        assertNotNull(booking.getId());
    }

    @Test
    public void updateBooking() {
        BookingDtoOut bookingDtoOut = bookingService.createBooking(bookingDtoInTest, userBooker.getId());

        BookingDtoOut bookingDtoOutReturn = bookingService.updateBooking(true, bookingDtoOut.getId(), userOwner.getId());
        assertEquals(Status.APPROVED, bookingDtoOutReturn.getStatus());
        assertEquals(bookingDtoOut.getId(), bookingDtoOutReturn.getId());
        assertEquals(bookingDtoOut.getStart(), bookingDtoOutReturn.getStart());
    }

    @Test
    public void getBookingById() {
        BookingDtoOut bookingDtoOut = bookingService.createBooking(bookingDtoInTest, userBooker.getId());

        BookingDtoOut bookingDtoOutReturn = bookingService.getBookingById(bookingDtoOut.getId(), userOwner.getId());
        assertEquals(bookingDtoOut.getId(), bookingDtoOutReturn.getId());
        assertEquals(bookingDtoOut.getStart(), bookingDtoOutReturn.getStart());
    }

    @Test
    public void getAllByBooker() {
        PageRequest page = PageRequest.of(0, 1);
        BookingDtoOut bookingDtoOut = bookingService.createBooking(bookingDtoInTest, userBooker.getId());

        List<BookingDtoOut> targetBookings = bookingService.getAllByBooker(userBooker.getId(), "ALL", page);
        assertEquals(1, targetBookings.size());
        assertEquals(bookingDtoOut.getId(), targetBookings.get(0).getId());
        assertEquals(bookingDtoOut.getBooker().getId(), targetBookings.get(0).getBooker().getId());
    }

    @Test
    public void getAllByOwner() {
        PageRequest page = PageRequest.of(0, 1);
        BookingDtoOut bookingDtoOut = bookingService.createBooking(bookingDtoInTest, userBooker.getId());

        List<BookingDtoOut> targetBookings = bookingService.getAllByOwner(userOwner.getId(), "ALL", page);
        assertEquals(1, targetBookings.size());
        assertEquals(bookingDtoOut.getId(), targetBookings.get(0).getId());
        assertEquals(bookingDtoOut.getBooker().getId(), targetBookings.get(0).getBooker().getId());
    }
}