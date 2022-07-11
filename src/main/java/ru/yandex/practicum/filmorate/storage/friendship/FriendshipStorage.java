package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Friendship;
import ru.yandex.practicum.filmorate.model.FriendshipStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipStorage {
    List<Friendship> findAll();

    long create(long userId, long friendId);

    String updateFriendshipStatus(Long friendshipId);
}
