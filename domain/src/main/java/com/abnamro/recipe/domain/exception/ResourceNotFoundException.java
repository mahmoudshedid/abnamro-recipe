package com.abnamro.recipe.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends RuntimeException implements CodeException {
    private static final long serialVersionUID = -9109730527834422478L;

    private HttpStatus status = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
