package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@Validated
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {

    public final UserService userService;


    @PostMapping
    public User createUser(@Valid @RequestBody UserDto userDto) {
        return userService.createUser(userDto);
    }

    @PatchMapping("/{userId}")
    public User updateUser(@RequestBody UserDto userDto, @PathVariable int userId) {
        return userService.updateUser(userDto, userId);
    }

    @GetMapping("/{userId}")
    public User getUserId(@Valid @PathVariable int userId) {
        return userService.getUserId(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @DeleteMapping("/{userId}")
    public void removeUserId(@Valid @PathVariable int userId) {
        userService.removeUserId(userId);
    }


}
