package com.prova.senior.sistemas.nivel1.errorHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@ControllerAdvice
public class ErrorExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Map<String, Object> handleHttpMessageNotReadableException(ConstraintViolationException exception) {
        List<String> errorsList = new ArrayList<>();

        exception.getConstraintViolations().forEach((error) -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();

            errorsList.add(String.format("Campo %s - Mensagem: %s", fieldName, errorMessage));
        });

        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("date", LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm:ss")
                        .withLocale(new Locale("pt", "BR"))));
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        errorResponse.put("error", exception.getClass().getCanonicalName());
        errorResponse.put("message", errorsList.toString());

        return errorResponse;
    }
}
