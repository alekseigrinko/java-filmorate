package ru.yandex.practicum.filmorate.exeption;

public class ErrorException extends RuntimeException{
    public ErrorException(String message) {
        super(message);
    }
}
