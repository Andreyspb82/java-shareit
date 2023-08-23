package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@Data
public class UserService {

    private final UserStorage userStorage;

    public User createUser(UserDto userDto) {
        return userStorage.putUser(UserMapper.mapToNewUser(userDto));
    }

    public User updateUser(UserDto userDto, long userId) {
        User oldUser = userStorage.getUserById(userId);
        return userStorage.updateUser(UserMapper.mapToUpdateUser(userDto, oldUser));
    }

    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void removeUser(long id) {
        userStorage.removeUser(id);
    }

}
