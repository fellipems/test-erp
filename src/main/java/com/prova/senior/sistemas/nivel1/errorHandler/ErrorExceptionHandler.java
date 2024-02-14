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

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<?> handleException(Exception exception, HttpServletRequest request) {
//        StandardError standardErrorBuilder = StandardError.Builder
//                .newBuilder()
//                .withTimestamp(new Date().getTime())
//                .withStatus(HttpStatus.NOT_FOUND.value())
//                .withMessage("Ocorreu um erro inesperado, tente novamente mais tarde " + exception.getMessage())
//                .withError(exception.getClass().getCanonicalName())
//                .withPath(request.getRequestURI()).build();
//
//        return new ResponseEntity<>(standardErrorBuilder, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(HttpMessageNotReadableException.class)
//    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception, HttpServletRequest request) {
//        StandardError standardErrorBuilder = StandardError.Builder
//                .newBuilder()
//                .withTimestamp(new Date().getTime())
//                .withStatus(HttpStatus.BAD_REQUEST.value())
//                .withMessage("Não é possível converter texto no campo número")
//                .withError(exception.getClass().getCanonicalName())
//                .withPath(request.getRequestURI()).build();
//
//        return new ResponseEntity<>(standardErrorBuilder, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(DadoObrigatorioNaoInformadoException.class)
//    public ResponseEntity<?> handleDadoObrigatorioNaoInformadoException(DadoObrigatorioNaoInformadoException exception, HttpServletRequest request) {
//        StandardError standardErrorBuilder = StandardError.Builder
//                .newBuilder()
//                .withTimestamp(new Date().getTime())
//                .withStatus(HttpStatus.BAD_REQUEST.value())
//                .withMessage(exception.getMessage())
//                .withError(exception.getClass().getCanonicalName())
//                .withPath(request.getRequestURI()).build();
//
//        return new ResponseEntity<>(standardErrorBuilder, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(UsuarioNaoInformadoException.class)
//    public ResponseEntity<?> handleUsuarioNaoInformadoException(UsuarioNaoInformadoException exception, HttpServletRequest request) {
//        StandardError standardErrorBuilder = StandardError.Builder
//                .newBuilder()
//                .withTimestamp(new Date().getTime())
//                .withStatus(HttpStatus.BAD_REQUEST.value())
//                .withMessage(exception.getMessage())
//                .withError(exception.getClass().getCanonicalName())
//                .withPath(request.getRequestURI()).build();
//
//        return new ResponseEntity<>(standardErrorBuilder, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(UsuarioNaoEncontradoException.class)
//    public ResponseEntity<?> handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException exception, HttpServletRequest request) {
//        StandardError standardErrorBuilder = StandardError.Builder
//                .newBuilder()
//                .withTimestamp(new Date().getTime())
//                .withStatus(HttpStatus.NOT_FOUND.value())
//                .withMessage(exception.getMessage())
//                .withError(exception.getClass().getCanonicalName())
//                .withPath(request.getRequestURI()).build();
//
//        return new ResponseEntity<>(standardErrorBuilder, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(EnderecoNaoEncontradoException.class)
//    public ResponseEntity<?> handleEnderecoNaoEncontradoException(EnderecoNaoEncontradoException exception, HttpServletRequest request) {
//        StandardError standardErrorBuilder = StandardError.Builder
//                .newBuilder()
//                .withTimestamp(new Date().getTime())
//                .withStatus(HttpStatus.NOT_FOUND.value())
//                .withMessage(exception.getMessage())
//                .withError(exception.getClass().getCanonicalName())
//                .withPath(request.getRequestURI()).build();
//
//        return new ResponseEntity<>(standardErrorBuilder, HttpStatus.NOT_FOUND);
//    }
}
