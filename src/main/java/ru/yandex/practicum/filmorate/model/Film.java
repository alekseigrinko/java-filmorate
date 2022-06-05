package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {

    private int id;
    @NotEmpty
    private final String name;
    private final String description;
    private final LocalDate releaseDate;
    private final int duration;

}
