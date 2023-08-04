package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;

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
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PatchMapping ("/{userId}")
    public User updateUser(@Valid @RequestBody User user, @PathVariable int userId) {
       return userService.updateUser(user, userId);
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
