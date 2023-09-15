package ru.practicum.shareit.item.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    ItemRequestRepository itemRequestRepository;

    Item itemTest;

    User userTest;

    ItemRequest itemRequest;

    @BeforeEach
    public void setUp() {
        userTest = new User();
        userTest.setName("Name");
        userTest.setEmail("email@mail.ru");

        itemRequest = new ItemRequest();
        itemRequest.setDescription("request description");
        itemRequest.setRequestor(userTest);

        itemTest = new Item();
        itemTest.setName("item");
        itemTest.setDescription("description");
        itemTest.setAvailable(true);
        itemTest.setOwner(userTest);
        itemTest.setRequest(itemRequest);
    }

    @Test
    public void findByOwnerIdOrderByIdAsc() {
        PageRequest page = PageRequest.of(0, 1);
        long itemId = itemRepository.save(itemTest).getId();
        long ownerId = userRepository.save(userTest).getId();
        long itemRequestId = itemRequestRepository.save(itemRequest).getId();

        List<Item> targetItems = itemRepository.findByOwnerIdOrderByIdAsc(ownerId, page);

        assertEquals(itemTest.getName(), targetItems.get(0).getName());
        assertEquals(itemTest.getDescription(), targetItems.get(0).getDescription());
        assertEquals(itemTest.getAvailable(), targetItems.get(0).getAvailable());
        assertEquals(itemTest.getOwner().getName(), targetItems.get(0).getOwner().getName());
        assertEquals(itemTest.getRequest().getDescription(), targetItems.get(0).getRequest().getDescription());
    }

    @Test
    public void search() {
        PageRequest page = PageRequest.of(0, 1);
        String query = itemTest.getName();
        long itemId = itemRepository.save(itemTest).getId();
        long ownerId = userRepository.save(userTest).getId();
        long itemRequestId = itemRequestRepository.save(itemRequest).getId();

        List<Item> targetItems = itemRepository.search(query, page);

        assertEquals(itemTest.getName(), targetItems.get(0).getName());
        assertEquals(itemTest.getDescription(), targetItems.get(0).getDescription());
        assertEquals(itemTest.getAvailable(), targetItems.get(0).getAvailable());
        assertEquals(itemTest.getOwner().getName(), targetItems.get(0).getOwner().getName());
        assertEquals(itemTest.getRequest().getDescription(), targetItems.get(0).getRequest().getDescription());
    }

    @Test
    public void findByRequestId() {
        long itemId = itemRepository.save(itemTest).getId();
        long ownerId = userRepository.save(userTest).getId();
        long itemRequestId = itemRequestRepository.save(itemRequest).getId();

        List<Item> targetItems = itemRepository.findByRequestId(itemRequestId);

        assertEquals(itemTest.getName(), targetItems.get(0).getName());
        assertEquals(itemTest.getDescription(), targetItems.get(0).getDescription());
        assertEquals(itemTest.getAvailable(), targetItems.get(0).getAvailable());
        assertEquals(itemTest.getOwner().getName(), targetItems.get(0).getOwner().getName());
        assertEquals(itemTest.getRequest().getDescription(), targetItems.get(0).getRequest().getDescription());
    }
}