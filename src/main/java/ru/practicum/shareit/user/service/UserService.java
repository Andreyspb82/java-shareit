package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    public User createUser(UserDto userDto);

    public User updateUser(UserDto userDto, long userId);

    public User getUserById(long userId);

    public List<User> getUsers();

    public void removeUser(long id);
}
