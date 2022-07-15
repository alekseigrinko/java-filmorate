package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private long id;
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private String name;
    private final LocalDate birthday;

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
