package ru.itis.exceptions;

public class EntityNotEmptyException extends InvalidDataException {
    public EntityNotEmptyException(String message) {
        super(message);
    }
}
