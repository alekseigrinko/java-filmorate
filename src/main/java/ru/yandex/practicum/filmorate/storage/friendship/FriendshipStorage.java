package ru.yandex.practicum.filmorate.storage.friendship;

import ru.yandex.practicum.filmorate.model.Friendship;

import java.util.List;

public interface FriendshipStorage {
    List<Friendship> findAll();

    long create(long userId, long friendId);

    String updateFriendshipStatus(Long friendshipId);


    void deleteFriendshipByUserIdFriendId(long userId, long friendId);

    List<Friendship> getFriendshipByUserId (long id);

    List<Friendship> findCommonFriendsByFriendIdAndUserId(long id, long friendId);
}
