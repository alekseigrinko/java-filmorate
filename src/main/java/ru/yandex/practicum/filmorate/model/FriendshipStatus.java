package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class FriendshipStatus {
    private final long friendShipStatusId;
    private final String name;
}
