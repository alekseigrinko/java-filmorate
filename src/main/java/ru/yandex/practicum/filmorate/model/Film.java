package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {

    private int id;
    @NotEmpty
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;
    private Set<Integer> likes = new HashSet<>();
}
