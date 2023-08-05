package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
@Data
public class UserService {

    private final UserStorage userStorage;

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user, int userId) {
        return userStorage.updateUser(user, userId);
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
