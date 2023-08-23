package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorage implements UserStorage {

    private final UserRepository repository;

    @Override
    public User putUser(User user) {

        Set<ConstraintViolation<User>> violations = Validation.buildDefaultValidatorFactory().getValidator().validate(user);
        if (!violations.isEmpty()) {
            throw new ValidationException("User data not validated");
        }
        return repository.save(user);
    }


    @Override
    public User updateUser(User user) {
       // repository.updateUser(user.getEmail(), user.getName(), user.getId());
        repository.save(user);
        log.info("PATCH/users request, user updated");
        return getUserById(user.getId());
    }

    @Override
    public User getUserById(long userId) {

        Optional<User> user = repository.findById(userId);
        if (user.isEmpty()) {
            throw new NotFoundException("User with Id = " + userId + " does not exist");
        }
        log.info("GET/users/id request");
        return user.get();
    }

    @Override
    public List<User> getUsers() {
        log.info("GET/users request, (list of users)");
        return repository.findAll();
    }

    @Override
    public void removeUser(long userId) {
        log.info("DELETE/users request, user deleted");
        repository.deleteById(userId);
    }

//    @Transactional
//    @Override
//    public UserDto add(UserDto userDto) {
//        validationUser(userDto);
//        validationEmailNull(userDto);
//        validationEmail(userDto);
//        User user = UserMapper.toUser(userDto);
//        try {
//            userDto = UserMapper.toUserDto(userRepository.save(user));
//        } catch (DataIntegrityViolationException e) {
//            log.info("Дубликат электронного адресса пользователя");
//            throw new DuplicateEmailException("Дубликат электронного адреса пользователя");
//        }
//        return userDto;
//    }

}
