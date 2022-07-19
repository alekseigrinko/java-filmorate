package ru.yandex.practicum.filmorate.storage.friendship;

import java.util.List;

public interface FriendshipStorage {

    List<Long> findAll();

    void create(long userId, long friendId);

    List<Long> getFriendByUserId (long id);

    void deleteFriendshipByUserIdFriendId(long userId, long friendId);

    List<Long> findCommonFriendsByFriendIdAndUserId(long id, long friendId);
}
