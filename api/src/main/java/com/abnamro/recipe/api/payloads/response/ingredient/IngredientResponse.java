package com.abnamro.recipe.api.payloads.response.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponse {
    private long id;
    private String name;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public IngredientResponse(Ingredient ingredient) {
        this.id = ingredient.getId();
        this.name = ingredient.getName();
        this.description = ingredient.getDescription();
        this.createdBy = ingredient.getCreatedBy();
        this.createdAt = ingredient.getCreatedAt();
        this.updatedBy = ingredient.getUpdatedBy();
        this.updatedAt = ingredient.getUpdatedAt();
    }
}
