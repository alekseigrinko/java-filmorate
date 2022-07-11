package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class FilmLike {
    private final long filmLikeId;
    private long filmId;
    private long userId;
}
