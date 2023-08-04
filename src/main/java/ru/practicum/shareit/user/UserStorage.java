package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {

    User createUser(User user);

    User updateUser(User user, int userId);

    User getUserId (int userId);

    List<User> getUsers();

    void removeUserId(int userId);


}
