package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoIn;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.booking.enums.Status;
import ru.practicum.shareit.booking.service.BookingService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    BookingDtoIn bookingDtoIn;

    BookingDtoOut bookingDtoOut;


    @BeforeEach
    public void setUp() {
        bookingDtoIn = new BookingDtoIn();
        bookingDtoIn.setStart(LocalDateTime.now().plusSeconds(2));
        bookingDtoIn.setEnd(LocalDateTime.now().plusSeconds(3));
        bookingDtoIn.setItemId(1L);

        bookingDtoOut = BookingDtoOut.builder()
                .start(bookingDtoIn.getStart().plusSeconds(2))
                .end(bookingDtoIn.getEnd().plusSeconds(3))
                .status(Status.WAITING)
                .build();
    }


    @Test
    public void createBooking() throws Exception {
        when(bookingService.createBooking(Mockito.any(BookingDtoIn.class), Mockito.anyLong())).thenReturn(bookingDtoOut);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    public void createBookingShouldReturnBadRequest() throws Exception {
        bookingDtoIn.setStart(LocalDateTime.now().minusSeconds(10));

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateBooking() throws Exception {
        bookingDtoOut.setStatus(Status.APPROVED);
        when(bookingService.updateBooking(Mockito.any(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(bookingDtoOut);

        mvc.perform(patch("/bookings/1?approved=true")
                        .content(mapper.writeValueAsString(bookingDtoIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.APPROVED))));
    }

    @Test
    public void getBookingById() throws Exception {
        when(bookingService.getBookingById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(bookingDtoOut);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$.status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    public void getAllByBookerStateIsAll() throws Exception {
        List<BookingDtoOut> bookings = List.of(bookingDtoOut);
        PageRequest page = PageRequest.of(0, 1);

        when(bookingService.getAllByBooker(Mockito.anyLong(), Mockito.anyString(), Mockito.any(PageRequest.class)))
                .thenReturn(bookings);

        mvc.perform(get("/bookings?state=ALL")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    public void getAllByBookerStateIsEmpty() throws Exception {
        List<BookingDtoOut> bookings = List.of(bookingDtoOut);
        PageRequest page = PageRequest.of(0, 1);

        when(bookingService.getAllByBooker(Mockito.anyLong(), Mockito.anyString(), Mockito.any(PageRequest.class)))
                .thenReturn(bookings);

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    public void getAllByBookerWithFromAndSize() throws Exception {
        List<BookingDtoOut> bookings = List.of(bookingDtoOut);
        PageRequest page = PageRequest.of(0, 1);

        when(bookingService.getAllByBooker(Mockito.anyLong(), Mockito.anyString(), Mockito.any(PageRequest.class)))
                .thenReturn(bookings);

        mvc.perform(get("/bookings?from=0&size=1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    public void getAllByOwnerIsAll() throws Exception {
        List<BookingDtoOut> bookings = List.of(bookingDtoOut);
        PageRequest page = PageRequest.of(0, 1);

        when(bookingService.getAllByOwner(Mockito.anyLong(), Mockito.anyString(), Mockito.any(PageRequest.class)))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner?state=ALL")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    public void getAllByOwnerIsEmpty() throws Exception {
        List<BookingDtoOut> bookings = List.of(bookingDtoOut);
        PageRequest page = PageRequest.of(0, 1);

        when(bookingService.getAllByOwner(Mockito.anyLong(), Mockito.anyString(), Mockito.any(PageRequest.class)))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))));
    }

    @Test
    public void getAllByOwnerWithFromAndSize() throws Exception {
        List<BookingDtoOut> bookings = List.of(bookingDtoOut);
        PageRequest page = PageRequest.of(0, 1);

        when(bookingService.getAllByOwner(Mockito.anyLong(), Mockito.anyString(), Mockito.any(PageRequest.class)))
                .thenReturn(bookings);

        mvc.perform(get("/bookings/owner?from=0&size=1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDtoIn.getId()), Long.class))
                .andExpect(jsonPath("$[0].status", is(String.valueOf(Status.WAITING))));
    }
}