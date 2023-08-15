package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int userId = 1;

    @Override
    public int getNextId() {
        return userId++;
    }

    @Override
    public User putUser(User user) {
        users.put(user.getId(), user);
        log.info("Получен запрос POST /users, добавлен пользователь");
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        log.info("Получен запрос PATCH /users, обновлен пользователь");
        return users.get(user.getId());
    }

    @Override
    public User getUserById(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with Id = " + userId + " does not exist");
        }
        return users.get(userId);
    }

    @Override
    public List<User> getUsers() {
        log.info("Получен запрос GET /users, (список ползователей)");
        return new ArrayList<>(users.values());
    }

    @Override
    public void removeUser(int userId) {
        if (!users.containsKey(userId)) {
            throw new NotFoundException("User with Id = " + userId + " does not exist");
        }
        log.info("Получен запрос DELETE /users, удален пользоваталь");
        users.remove(userId);
    }

}
