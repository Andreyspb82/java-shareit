package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Data
public class UserService {

    private final UserStorage userStorage;

    private int userId = 1;

    private int getNextId() {
        return userId++;
    }

    public User createUser(UserDto userDto) {
        List<User> users = userStorage.getUsers();

        for (User user1 : users) {
            if (user1.getEmail().equals(userDto.getEmail())) {
                throw new ConflictException("Пользователь с email = " + userDto.getEmail() + " уже существует");
            }
        }

        User user = new User(
                getNextId(),
                userDto.getName(),
                userDto.getEmail()
        );

        return userStorage.putUser(user);
    }

    public User updateUser(UserDto userDto, int userId) {
        User oldUser = userStorage.getUserById(userId);

        List<User> users = userStorage.getUsers();

        for (User user1 : users) {
            if (userId != user1.getId()) {
                if (user1.getEmail().equals(userDto.getEmail())) {
                    throw new ConflictException("Пользователь с email = " + userDto.getEmail() + " уже существует");
                }
            }
        }

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
        return userStorage.updateUser(oldUser);
    }

    public User getUserById(int userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public void removeUser(int id) {
        userStorage.removeUser(id);
    }

}
