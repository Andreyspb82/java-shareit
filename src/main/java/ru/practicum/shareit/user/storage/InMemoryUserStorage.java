package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    private int userId = 1;

    private int getNextId() {
        return userId++;
    }

    @Override
    public User createUser(UserDto userDto) {

        for (User user1 : users.values()) {
            if (user1.getEmail().equals(userDto.getEmail())) {
                throw new ConflictException("Пользователь с email = " + userDto.getEmail() + "уже существует");
            }
        }

        User user = new User(
                getNextId(),
                userDto.getName(),
                userDto.getEmail()
        );

        users.put(user.getId(), user);
        log.info("Получен запрос POST /users, добавлен пользователь");
        return user;
    }

    @Override
    public User updateUser(UserDto userDto, int userId) {

        if (!users.containsKey(userId)) {
            log.warn("Пользователя с Id = " + userId + " нет");
            throw new NotFoundException("Пользователя с Id = " + userId + " нет");
        }

        for (User user1 : users.values()) {
            if (userId != user1.getId()) {
                if (user1.getEmail().equals(userDto.getEmail())) {
                    throw new ConflictException("Пользователь с email = " + userDto.getEmail() + "уже существует");
                }
            }
        }

        User oldUser = users.get(userId);
        if (userDto.getEmail() != null) {
            oldUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            oldUser.setName(userDto.getName());
        }

        Set<ConstraintViolation<User>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(oldUser);
        if (!violations.isEmpty()) {
            throw new ValidationException("Данные пользователя не прошли валидацию");
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
