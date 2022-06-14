package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    private static User user;
    private static UserController userController;
    private InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();
    private UserService userService = new UserService (inMemoryUserStorage);


    @BeforeEach
    public void SetUp() {
        user = new User("mail@mail.ru", "dolore", LocalDate.of(1946, 8, 20));
        user.setName("Nick Name");
        userController = new UserController(userService);
    }

    @Test
    void addUser() {
        User testUser = userController.create(user);
        user.setId(testUser.getId());
        Assertions.assertTrue(userController.getUserService().getInMemoryUserStorage().getUsers().containsValue(user));
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void addUserWithoutName() {
        user = new User("mail@mail.ru", "dolore", LocalDate.of(1946, 8, 20));
        user.setName("");
        User testUser = userController.create(user);
        Assertions.assertTrue(userController.getUserService().getInMemoryUserStorage().getUsers().containsValue(testUser));
        Assertions.assertEquals(userController.getUserService().getInMemoryUserStorage().getUsers().get(testUser.getId()).getName(),
                userController.getUserService().getInMemoryUserStorage().getUsers().get(testUser.getId()).getLogin());
    }

    @Test
    void updateUser() {
        User testUser = userController.create(user);
        testUser.setName("updateName");
        userController.update(testUser);
        Assertions.assertTrue(userController.getUserService().getInMemoryUserStorage().getUsers().containsValue(testUser));
        Assertions.assertEquals(userController.getUserService().getInMemoryUserStorage().getUsers().get(testUser.getId()).getName()
                , "updateName");
    }

    @Test
    void addUserIncorrectBirthday() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            user = new User("mail@mail.ru", "dolore", LocalDate.now().plusDays(1));
            user.setName("Nick Name");
            userController.create(user);
        });
        Assertions.assertEquals("Дата дня рождения не наступила!", thrown.getMessage());
    }

    @Test
    void addFilmIncorrectLogin() {
        final ValidationException thrown = assertThrows(ValidationException.class, () -> {
            user = new User("mail@mail.ru", "dol ore", LocalDate.of(1946, 8, 20));
            user.setName("Nick Name");
            userController.create(user);
        });
        Assertions.assertEquals("Ошибка написания логина!", thrown.getMessage());
    }

    @Test
    void updateUserIncorrectId() {
        final ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> {
            userController.create(user);
            user = new User("mail@mail.ru", "dolore", LocalDate.of(1946, 8, 20));
            user.setName("Nick Name");
            user.setId(-1);
            userController.update(user);
        });
        Assertions.assertEquals("Пользователя с таким ID (" + user.getId() + ")" +
                " не зарегистрировано!", thrown.getMessage());
    }

    @Test
    void addFilmIncorrectEmail() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        user = new User("это-неправильный?эмейл@", "dolore", LocalDate.of(1946, 8, 20));
        user.setName("Nick Name");
        Set<ConstraintViolation<User>> violations = validator.validate(userController.create(user));
        System.out.println(violations);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    void findAll() {
        userController.create(user);
        List<User> testList = userController.findAll();
        Assertions.assertEquals(1, userController.getUserService().getInMemoryUserStorage().getUsers().size());
        Assertions.assertEquals(testList.get(0), userController.getUserService().getInMemoryUserStorage().getUsers().get(user.getId()));
    }
}
