package com.alex.project.taskmanagerproject.exception;


import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @MessageExceptionHandler(MethodArgumentNotValidException.class)
    @SendToUser("/queue/errors")
    public List<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex){
        assert ex.getBindingResult() != null;
        return ex.getBindingResult().getAllErrors().stream()
                .map(error -> {
                    Map<String, String> errorMap = new HashMap<>();
                    if(error instanceof FieldError fieldError){
                        errorMap.put("field", fieldError.getField());
                    }else{
                        errorMap.put("error", "global");
                    }
                    errorMap.put("message", error.getDefaultMessage());
                    return errorMap;
                })
                .collect(Collectors.toList());
    }


    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptionForHttp(org.springframework.web.bind.MethodArgumentNotValidException ex){
        Map<String, String> errors = new HashMap<>();

        assert ex.getBindingResult() != null;
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptionForHttp(RuntimeException ex){
        Map<String, String> errors = new HashMap<>();
        if(ex.getMessage().startsWith("Email")){
            errors.put("email", ex.getMessage());
        }else if(ex.getMessage().startsWith("Username")){
            errors.put("nickname", ex.getMessage());
        }
        return ResponseEntity.badRequest().body(errors);
    }
}
