package com.abnamro.recipe.api.payloads.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
public class ExceptionResponse {
    private String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
