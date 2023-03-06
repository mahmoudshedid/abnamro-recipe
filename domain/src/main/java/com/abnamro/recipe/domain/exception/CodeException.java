package com.abnamro.recipe.domain.exception;

import org.springframework.http.HttpStatus;

public interface CodeException {
    HttpStatus getStatus();
}
