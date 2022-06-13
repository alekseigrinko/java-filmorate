package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private int id;
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private String name;
    private final LocalDate birthday;
    private Set<Integer> friends = new HashSet<>();
}
