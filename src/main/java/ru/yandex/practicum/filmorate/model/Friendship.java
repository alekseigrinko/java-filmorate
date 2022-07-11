package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class Friendship {
    private final long friendshipId;
    private long userId;
    private long friendId;
    private long getFriendshipStatusId;
}
