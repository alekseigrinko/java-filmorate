package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exeption.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserControllerTest {

    /*private static User user;
    private static UserController userController;
    private UserService userService = new UserService(new InMemoryUserStorage());


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
        Assertions.assertTrue(userService.getMapUsers().containsValue(user));
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void addUserWithoutName() {
        user = new User("mail@mail.ru", "dolore", LocalDate.of(1946, 8, 20));
        user.setName("");
        User testUser = userController.create(user);
        Assertions.assertTrue(userService.getMapUsers().containsValue(testUser));
        Assertions.assertEquals(userService.getMapUsers().get(testUser.getId()).getName(),
                userService.getMapUsers().get(testUser.getId()).getLogin());
    }

    @Test
    void updateUser() {
        User testUser = userController.create(user);
        testUser.setName("updateName");
        userController.update(testUser);
        Assertions.assertTrue(userService.getMapUsers().containsValue(testUser));
        Assertions.assertEquals(userService.getMapUsers().get(testUser.getId()).getName()
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
        Assertions.assertEquals(1, userService.getMapUsers().size());
        Assertions.assertEquals(testList.get(0), userService.getMapUsers().get(user.getId()));
    }

    @Test
    void getUser() {
        userController.create(user);
        User testUser = userController.getUserById(user.getId());
        Assertions.assertEquals(user, testUser);
    }

    @Test
    void checkFriend() {
        User testUser = new User("test@mail.ru", "test", LocalDate.of(1946, 8, 20));
        testUser.setName("Test Name");
        userController.create(user);
        userController.create(testUser);
        userController.putFriendByIdAndUserId(user.getId(), testUser.getId());
        boolean test = false;
        if (user.getFriends().get(0) == testUser.getId()) {
            test = true;
        }
        Assertions.assertTrue(test);
        User testUser2 = new User("test2@mail.ru", "test2", LocalDate.of(1946, 8, 20));
        testUser2.setName("Test2 Name");
        userController.create(testUser2);
        userController.putFriendByIdAndUserId(user.getId(), testUser2.getId());
        userController.putFriendByIdAndUserId(testUser.getId(), testUser2.getId());
        List<User> testUsers = userController.findFriendsByUserId(user.getId());
        Assertions.assertEquals(2, testUsers.size());
        testUsers = userController.findCommonFriendsByFriendIdAndUserId(user.getId(), testUser.getId());
        Assertions.assertEquals(1, testUsers.size());
        userController.deleteFriendByIdAndUserId(user.getId(), testUser.getId());
        userController.deleteFriendByIdAndUserId(user.getId(), testUser2.getId());
        Assertions.assertTrue(user.getFriends().isEmpty());
    }
*/
}
