package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class MpaRating {
    private final long mpaRatingId;
    private String name;
}
