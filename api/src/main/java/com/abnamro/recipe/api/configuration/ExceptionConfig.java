package com.abnamro.recipe.api.configuration;

import com.abnamro.recipe.api.payloads.response.ExceptionResponse;
import com.abnamro.recipe.domain.exception.CodeException;
import com.abnamro.recipe.domain.exception.ResourceNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionConfig {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleNotFoundException(ResourceNotFoundException exception) {
        HttpStatus status = exception.getStatus() == null ? HttpStatus.NOT_FOUND : exception.getStatus();
        return buildResponse(exception.getMessage(), status);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            InvalidDataAccessApiUsageException.class
    })
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleArgumentException(Exception ex) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleDataIntegrityViolationException(Exception ex) {
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            String message = "Delete relationships valuation";
            return buildResponse(message, HttpStatus.BAD_REQUEST);
        }

        return handleGlobalException();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return buildResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public final ResponseEntity<ExceptionResponse> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        return buildResponse(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "Invalid request, properties not sufficient";
        return buildResponse(message, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException exp) {
        if (exp instanceof CodeException) {
            return buildResponse(exp.getMessage(), ((CodeException) exp).getStatus());
        }

        String message = "Internal server error";
        return buildResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleGlobalException() {
        String message = "Internal server error";
        return buildResponse(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(String message, HttpStatus status) {
        return new ResponseEntity<>(new ExceptionResponse(message), status);
    }
}
