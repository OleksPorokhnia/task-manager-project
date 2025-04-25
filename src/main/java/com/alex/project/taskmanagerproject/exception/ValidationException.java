package com.alex.project.taskmanagerproject.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.stream.Collectors;

public class ValidationException extends RuntimeException {
    private final List<String> messages;

    public ValidationException(Errors errors) {
        this.messages = errors.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
    }

    public List<String> getMessages() {
        return messages;
    }
}
