package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Film {

    private long id;
    @NotEmpty
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Set<Long> likes = new HashSet<>();
}
