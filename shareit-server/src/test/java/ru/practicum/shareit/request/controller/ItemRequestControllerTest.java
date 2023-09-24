package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoIn;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequestTest;

    private ItemRequestDtoIn itemRequestDtoIn;

    @BeforeEach
    public void setUp() {
        itemRequestDtoIn = ItemRequestDtoIn.builder()
                .id(1L)
                .description("request description")
                .build();


        itemRequestTest = ItemRequestDto.builder()
                .id(itemRequestDtoIn.getId())
                .description(itemRequestDtoIn.getDescription())
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    public void createItemRequest() throws Exception {
        when(itemRequestService.createItemRequest(Mockito.any(ItemRequestDtoIn.class), Mockito.anyLong())).thenReturn(itemRequestTest);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDtoIn))
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestTest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestTest.getDescription())));
    }

    @Test
    public void getAllByRequestorId() throws Exception {
        List<ItemRequestDto> itemRequestDtos = List.of(itemRequestTest);

        when(itemRequestService.getAllByRequestorId(Mockito.anyLong())).thenReturn(itemRequestDtos);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestTest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestTest.getDescription())));
    }

    @Test
    public void getAllByRequestsByUserId() throws Exception {
        List<ItemRequestDto> itemRequestDtos = List.of(itemRequestTest);
        PageRequest page = PageRequest.of(0, 1);

        when(itemRequestService.getAllByNotRequestorId(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(itemRequestDtos);
        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestTest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestTest.getDescription())));
    }

    @Test
    public void getAllByRequestsByUserIdWithFromAndSize() throws Exception {
        List<ItemRequestDto> itemRequestDtos = List.of(itemRequestTest);
        PageRequest page = PageRequest.of(0, 1);

        when(itemRequestService.getAllByNotRequestorId(Mockito.anyLong(), Mockito.any(PageRequest.class))).thenReturn(itemRequestDtos);
        mvc.perform(get("/requests/all?from=0&size=1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestTest.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestTest.getDescription())));
    }

    @Test
    public void getRequestById() throws Exception {
        when(itemRequestService.getRequestById(Mockito.anyLong(), Mockito.anyLong())).thenReturn(itemRequestTest);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestTest.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestTest.getDescription())));
    }
}