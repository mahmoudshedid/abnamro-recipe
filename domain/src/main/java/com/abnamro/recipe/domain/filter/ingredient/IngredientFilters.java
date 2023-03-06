package com.abnamro.recipe.domain.filter.ingredient;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class IngredientFilters {
    private String name;
    private String description;
    private String createdBy;
    private String updatedBy;
}
