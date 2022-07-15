package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friendship.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class FriendshipDbStorageTest {

    private final FriendshipDbStorage friendshipDbStorage;
    private final UserDbStorage userDbStorage;

    void createFriendship() {
        userDbStorage.create(new User(1,"mail@mail.ru", "dolore","Nick Name"
                , LocalDate.of(1946, 8, 20)));
        userDbStorage.create(new User(2,"mail2@mail.ru", "dolore2","Nick Name2"
                , LocalDate.of(1946, 8, 20)));
        friendshipDbStorage.create(1,2);
    }

    @Test
    void findAll() {
        Assertions.assertEquals(3, friendshipDbStorage.findAll().size());
    }

    @Test
    void deleteFriendshipByUserIdFriendId() {
        friendshipDbStorage.deleteFriendshipByUserIdFriendId(1,2);
        Assertions.assertEquals(2, friendshipDbStorage.findAll().size());
    }

    @Test
    void getFriendshipByUserId() {
        List<Friendship> testList = friendshipDbStorage.getFriendshipByUserId(1);
        Assertions.assertEquals(2, testList.size());
    }

    @Test
    void findCommonFriendsByFriendIdAndUserId() {
        createFriendship();
        userDbStorage.create(new User(3,"mail3@mail.ru", "dolore3","Nick Name3"
                , LocalDate.of(1946, 8, 20)));
        friendshipDbStorage.create(1,3);
        friendshipDbStorage.create(3,2);
        List<Friendship> testList = friendshipDbStorage.findCommonFriendsByFriendIdAndUserId(1,3);
        Assertions.assertEquals(1, testList.size());
    }
}