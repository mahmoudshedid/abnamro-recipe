package com.abnamro.recipe.api.payloads.response.recipe;

import com.abnamro.recipe.api.payloads.response.ingredient.IngredientResponse;
import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.entities.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecipeResponse {
    private long id;
    private String name;
    private String description;
    private ERecipeType type;
    private int numberOfServings;
    private Set<IngredientResponse> ingredients;
    private String createdBy;
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;

    public RecipeResponse(Recipe recipe) {
        this.id = recipe.getId();
        this.name = recipe.getName();
        this.description = recipe.getDescription();
        this.type = recipe.getType();
        this.numberOfServings = recipe.getNumberOfServings();
        this.ingredients = recipe.getIngredients() != null
                ? recipe.getIngredients().stream().map(IngredientResponse::new).collect(Collectors.toSet())
                : null;
        this.createdBy = recipe.getCreatedBy();
        this.createdAt = recipe.getCreatedAt();
        this.updatedBy = recipe.getUpdatedBy();
        this.updatedAt = recipe.getUpdatedAt();
    }
}
