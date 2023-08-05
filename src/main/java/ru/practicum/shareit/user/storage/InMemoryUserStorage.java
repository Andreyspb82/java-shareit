package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
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

    private int getNextId() {
        return userId++;
    }

    @Override
    public User createUser(User user) {

        for (User user1 : users.values()) {
            if (user1.getEmail().equals(user.getEmail())) {
                throw new ConflictException("Пользователь с email = " + user.getEmail() + "уже существует");
            }
        }

        if (user.getEmail() == null) {
            throw new ValidationException("У пользователя не указан email");
        }
        user.setId(getNextId());

        users.put(user.getId(), user);
        log.info("Получен запрос POST /users, добавлен пользователь");
        return user;
    }

    @Override
    public User updateUser(User user, int userId) {

        if (!users.containsKey(userId)) {
            log.warn("Пользователя с Id = " + user.getId() + " нет");
            throw new NotFoundException("Пользователя с Id = " + user.getId() + " нет");
        }

        for (User user1 : users.values()) {
            if (userId != user1.getId()) {
                if (user1.getEmail().equals(user.getEmail())) {
                    throw new ConflictException("Пользователь с email = " + user.getEmail() + "уже существует");
                }
            }
        }

        User oldUser = users.get(userId);
        if (user.getEmail() != null) {
            oldUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            oldUser.setName(user.getName());
        }

        users.put(userId, oldUser);
        return users.get(userId);
    }

    @Override
    public User getUserId(int userId) {
        if (!users.containsKey(userId)) {
            log.warn("Пользователя с Id = " + userId + " нет");
            throw new NotFoundException("Пользователя с Id = " + userId + " нет");
        }
        return users.get(userId);
    }

    @Override
    public List<User> getUsers() {
        log.info("Получен запрос GET /users, (список ползователей)");
        return new ArrayList<>(users.values());
    }

    @Override
    public void removeUserId(int userId) {
        if (!users.containsKey(userId)) {
            log.warn("Пользователя с Id = " + userId + " нет");
            throw new NotFoundException("Пользователя с Id = " + userId + " нет");
        }
        users.remove(userId);
    }

}
