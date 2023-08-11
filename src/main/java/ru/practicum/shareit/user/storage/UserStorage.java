package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(UserDto userDto);

    User updateUser(UserDto userDto, int userId);

    User getUserId(int userId);

    List<User> getUsers();

    void removeUserId(int userId);
}
