package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@Data
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(UserDto userDto) {
        return userRepository.save(UserMapper.mapToNewUser(userDto));
    }

    @Override
    public User updateUser(UserDto userDto, long userId) {
        User oldUser = getUserById(userId);
        User updateUser = UserMapper.mapToUpdateUser(userDto, oldUser);

        Set<ConstraintViolation<User>> violations = Validation
                .buildDefaultValidatorFactory().getValidator().validate(updateUser);
        if (!violations.isEmpty()) {
            throw new ValidationException("User data not validated");
        }
        log.info("PATCH/users request, user updated");
        return userRepository.save(updateUser);
    }

    @Override
    public User getUserById(long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with Id = " + userId + " does not exist");
        }
        log.info("GET/users/id request");
        return userRepository.findById(userId).get();
    }

    @Override
    public List<User> getUsers() {
        log.info("GET/users request, (list of users)");
        return userRepository.findAll();
    }

    @Override
    public void removeUser(long userId) {
        log.info("DELETE/users request, user deleted");
        userRepository.deleteById(userId);
    }
}
