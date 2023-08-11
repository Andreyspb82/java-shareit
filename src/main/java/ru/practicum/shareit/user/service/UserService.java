package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
        return userStorage.createUser(userDto);
    }

    public User updateUser(UserDto userDto, int userId) {
        return userStorage.updateUser(userDto, userId);
    }

    public User getUserId(int userId) {
        return userStorage.getUserId(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void removeUserId(int id) {
        userStorage.removeUserId(id);
    }



}
