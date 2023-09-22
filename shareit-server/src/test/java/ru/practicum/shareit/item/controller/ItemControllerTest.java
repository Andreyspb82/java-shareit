package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBooking;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
class ItemControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDtoTest;

    private ItemDtoBooking itemDtoBookingTest;

    private Comment comment;

    private User user;

    @BeforeEach
    public void setUp() {
        itemDtoTest = ItemDto.builder()
                .id(1L)
                .name("item")
                .description("description")
                .available(true)
                .requestId(2L)
                .build();

        itemDtoBookingTest = ItemDtoBooking.builder()
                .id(2L)
                .name("itemBooking")
                .description("descriptionBooking")
                .available(true)
                .build();

        user = User.builder()
                .name("user")
                .email("email@mail.ru")
                .build();

        comment = new Comment();
        comment.setId(1L);
        comment.setText("comment");
        comment.setAuthor(user);
    }


    @Test
    public void createItem() throws Exception {
        when(itemService.createItem(Mockito.any(ItemDto.class), Mockito.anyLong())).thenReturn(itemDtoTest);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDtoTest))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoTest.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoTest.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoTest.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoTest.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDtoTest.getRequestId()), Long.class));
    }

    @Test
    public void updateItem() throws Exception {
        itemDtoTest.setName("rename");
        itemDtoTest.setDescription("redescription");

        when(itemService.updateItem(Mockito.any(ItemDto.class), Mockito.anyLong())).thenReturn(itemDtoTest);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoTest))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoTest.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoTest.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoTest.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoTest.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDtoTest.getRequestId()), Long.class));
    }

    @Test
    public void updateItemShouldReturnNotFound() throws Exception {
        when(itemService.updateItem(Mockito.any(ItemDto.class), Mockito.anyLong())).thenThrow(new NotFoundException("error"));

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemDtoTest))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    public void getItemWithBookingById() throws Exception {
        when(itemService.getItemWithBookingById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemDtoBookingTest);

        mvc.perform(get("/items/1")
                        // .content(mapper.writeValueAsString(itemDtoBookingTest))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoBookingTest.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoBookingTest.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoBookingTest.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoBookingTest.getAvailable())));
    }

    @Test
    public void getItemsByUserId() throws Exception {
        List<ItemDtoBooking> items = List.of(itemDtoBookingTest);
        PageRequest page = PageRequest.of(0, 1);

        when(itemService.getItemsByUserId(Mockito.anyLong(), Mockito.any())).thenReturn(items);

        mvc.perform(get("/items?from=1&size=1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoBookingTest.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoBookingTest.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoBookingTest.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoBookingTest.getAvailable())));
    }

    @Test
    public void getItemsByQuery() throws Exception {
        List<ItemDto> items = List.of(itemDtoTest);
        PageRequest page = PageRequest.of(0, 1);
        String query = "query";

        when(itemService.getItemsByQuery(Mockito.anyString(), Mockito.any())).thenReturn(items);

        mvc.perform(get("/items/search?text=query&from=1&size=1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoTest.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoTest.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoTest.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoTest.getAvailable())));
    }

    @Test
    void createComment() throws Exception {
        CommentDto commentDto = CommentMapper.mapToCommentDto(comment);

        when(itemService.createComment(Mockito.any(Comment.class), Mockito.anyLong(), Mockito.anyLong())).thenReturn(commentDto);

        mvc.perform(post("/items/1/comment")
                        .content(mapper.writeValueAsString(comment))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }
}