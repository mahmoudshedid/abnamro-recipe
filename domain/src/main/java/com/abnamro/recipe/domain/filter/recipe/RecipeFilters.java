package com.abnamro.recipe.domain.filter.recipe;

import com.abnamro.recipe.domain.entities.ERecipeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecipeFilters {
    private String name;
    private String description;
    private ERecipeType type;
    private int numberOfServings;
    private String createdBy;
    private String updatedBy;
}
