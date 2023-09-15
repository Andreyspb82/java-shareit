package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mvc;

    User userTest1;

    @BeforeEach
    public void setUp() {
        userTest1 = new User();
        userTest1.setId(1L);
        userTest1.setName("Name");
        userTest1.setEmail("test@mail.ru");
    }

    @Test
    void createUser() throws Exception {
        when(userService.createUser(Mockito.any(UserDto.class))).thenReturn(userTest1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userTest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userTest1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userTest1.getName())))
                .andExpect(jsonPath("$.email", is(userTest1.getEmail())));
    }

    @Test
    void saveUserShouldReturnBadRequest() throws Exception {
        when(userService.createUser(Mockito.any(UserDto.class))).thenThrow(new ValidationException("error"));

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userTest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser() throws Exception {
        userTest1.setName("rename");
        userTest1.setEmail("reemail@mail.ru");

        when(userService.updateUser(Mockito.any(UserDto.class), Mockito.anyLong())).thenReturn(userTest1);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userTest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userTest1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userTest1.getName())))
                .andExpect(jsonPath("$.email", is(userTest1.getEmail())));
    }

    @Test
    void updateUserShouldReturnNotFound() throws Exception {
        when(userService.updateUser(Mockito.any(UserDto.class), Mockito.anyLong())).thenThrow(new NotFoundException("error"));

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userTest1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserById() throws Exception {
        when(userService.getUserById(Mockito.anyLong())).thenReturn(userTest1);

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userTest1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userTest1.getName())))
                .andExpect(jsonPath("$.email", is(userTest1.getEmail())));
    }

    @Test
    void getUserByIdShouldReturnNotFound() throws Exception {
        when(userService.getUserById(Mockito.anyLong())).thenThrow(new NotFoundException("error"));

        mvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void removeUser() throws Exception {
        mvc.perform(delete("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}