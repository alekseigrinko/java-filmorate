package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    User user;
    private final UserDbStorage userDbStorage;

    private void createUser(){
        userDbStorage.create(new User(1,"mail@mail.ru", "dolore","Nick Name"
                , LocalDate.of(1946, 8, 20)));
    }

    @Test
    void findAll() {
        createUser();
        List<User> testList = userDbStorage.findAll();
        Assertions.assertEquals(2, userDbStorage.findAll().size());
        Assertions.assertEquals(testList.get(0), userDbStorage.findAll().get(0));
    }

    @Test
    void create() {
        createUser();
        Assertions.assertEquals(1, userDbStorage.getUserById(1).getId());
    }

    @Test
    void update() {
        createUser();
        User testUser = userDbStorage.getUserById(1);
        testUser.setName("updateName");
        userDbStorage.update(testUser);
        Assertions.assertEquals(userDbStorage.getUserById(1).getName(), "updateName");
    }

    @Test
    void addUserIncorrectBirthday() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            user = new User(1,"mail@mail.ru", "dolore", "Nick Name"
                    , LocalDate.now().plusDays(1));
            userDbStorage.create(user);
        });
        Assertions.assertEquals("Дата дня рождения не наступила!", thrown.getMessage());
    }

    @Test
    void addFilmIncorrectLogin() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            user = new User(1,"mail@mail.ru", "dol ore", "Nick Name"
                    , LocalDate.of(1946, 8, 20));
            userDbStorage.create(user);
        });
        Assertions.assertEquals("Ошибка написания логина!", thrown.getMessage());
    }

    @Test
    void updateUserIncorrectId() {
        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            createUser();
            user = new User(-1, "mail@mail.ru", "dolore", "Nick Name"
                    , LocalDate.of(1946, 8, 20));
            userDbStorage.update(user);
        });
        Assertions.assertEquals("Пользователя с ID " + user.getId() + " не найдено!", thrown.getMessage());
    }

}