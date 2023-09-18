package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {"db.name=shareitTest"})
@Transactional
class UserServiceImplIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager em;

    private UserDto userDtoTest1;
    private UserDto userDtoTest2;

    private User userTest;

    @BeforeEach
    public void setUp() {
        userDtoTest1 = new UserDto();
        userDtoTest1.setName("NameDto1");
        userDtoTest1.setEmail("testDto1@mail.ru");

        userDtoTest2 = new UserDto();
        userDtoTest2.setName("NameDto2");
        userDtoTest2.setEmail("testDto2@mail.ru");

        userTest = new User();
        userTest.setName("Name");
        userTest.setEmail("email@mail.ru");
    }

    @Test
    void createUser() {
        userService.createUser(userDtoTest1);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query
                .setParameter("email", userDtoTest1.getEmail())
                .getSingleResult();

        assertEquals(userDtoTest1.getName(), user.getName());
        assertEquals(userDtoTest1.getEmail(), user.getEmail());
        assertNotNull(user.getId());
    }

    @Test
    void updateUser() {
        User user = userService.createUser(userDtoTest1);
        long userId = user.getId();

        userDtoTest1.setName("renameDto1");
        userDtoTest1.setEmail("reemailDto1@mail.ru");
        User userUpdate = userService.updateUser(userDtoTest1, userId);

        assertEquals("renameDto1", userUpdate.getName());
        assertEquals("reemailDto1@mail.ru", userUpdate.getEmail());
        assertEquals(userId, userUpdate.getId());
    }

    @Test
    void getUserById() {
        User user = userService.createUser(userDtoTest1);
        long userId = user.getId();

        User userById = userService.getUserById(userId);

        assertEquals(user.getName(), userById.getName());
        assertEquals(user.getEmail(), userById.getEmail());
        assertEquals(user.getId(), userById.getId());
    }

    @Test
    void getUsers() {
        List<UserDto> sourceUsers = List.of(userDtoTest1, userDtoTest2);
        for (UserDto userDto : sourceUsers) {
            userService.createUser(userDto);
        }

        List<User> targetUsers = userService.getUsers();

        assertEquals(sourceUsers.size(), targetUsers.size());
        for (UserDto sourceUser : sourceUsers) {
            assertThat(targetUsers, hasItem(allOf(
                    hasProperty("id", notNullValue()),
                    hasProperty("name", equalTo(sourceUser.getName())),
                    hasProperty("email", equalTo(sourceUser.getEmail()))
            )));
        }
    }

    @Test
    void removeUser() {
        User user = userService.createUser(userDtoTest1);
        long userId = user.getId();
        List<User> targetUsers = userService.getUsers();
        assertEquals(targetUsers.size(), 1);

        userService.removeUser(userId);
        List<User> removeUsers = userService.getUsers();
        assertTrue(removeUsers.isEmpty());
    }
}