package ru.itis.exceptions;

public class ProjectNotEmptyException extends InvalidDataException {
    public ProjectNotEmptyException(String message) {
        super(message);
    }
}
