package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User putUser(User user);

    User updateUser(User user);

    User getUserById(long userId);

    List<User> getUsers();

    void removeUser(long userId);
}
