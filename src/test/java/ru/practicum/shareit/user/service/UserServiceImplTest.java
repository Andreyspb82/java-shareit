package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    UserDto userDtoTest1;
    User userTest;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userRepository);
        userDtoTest1 = new UserDto();
        userDtoTest1.setName("NameDto1");
        userDtoTest1.setEmail("testDto1@mail.ru");

        userTest = new User();
        userTest.setName("Name");
        userTest.setEmail("email@mail.ru");
    }

    @Test
    public void createUser() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(UserMapper.mapToNewUser(userDtoTest1));
        User userReturn = userService.createUser(userDtoTest1);
        assertEquals(userDtoTest1.getName(), userReturn.getName());
        assertEquals(userDtoTest1.getEmail(), userReturn.getEmail());
    }

    @Test
    public void shouldReturnErrorInvalidName() {
        userDtoTest1.setName("");
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.createUser(userDtoTest1);
            }
        });
        assertEquals("User data not validated: не должно быть пустым", ex.getMessage());
    }

    @Test
    public void shouldReturnErrorInvalidEmail() {
        userDtoTest1.setEmail("email");
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.createUser(userDtoTest1);
            }
        });
        assertEquals("User data not validated: должно иметь формат адреса электронной почты", ex.getMessage());
    }

    @Test
    void updateUser() {
        userDtoTest1.setName("Rename");
        userDtoTest1.setEmail("reemail@mail.ru");

        when(userRepository.existsById(Mockito.anyLong())).thenReturn(true);
        when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.ofNullable(userTest));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(UserMapper.mapToUpdateUser(userDtoTest1, userTest));

        User userUpdate = userService.updateUser(userDtoTest1, 1L);
        assertEquals("Rename", userUpdate.getName());
        assertEquals("reemail@mail.ru", userUpdate.getEmail());
    }

    @Test
    public void shouldReturnErrorNotExistUser() {
        NotFoundException ex = assertThrows(NotFoundException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                userService.getUserById(1L);
            }
        });
        assertEquals("User with Id = 1 does not exist", ex.getMessage());
    }
}