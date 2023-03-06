package com.abnamro.recipe.domain.usecases.recipe;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import com.abnamro.recipe.domain.usecases.ingredient.ReadIngredientUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UpdateRecipeUseCase {

    private final ReadRecipeUseCase readRecipeUseCase;
    private final RecipeRepository recipeRepository;
    private final ReadIngredientUseCase readIngredientUseCase;

    public Recipe execute(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }
        Recipe currentRecipe = this.readRecipeUseCase.execute(recipe.getId());

        Set<Ingredient> ingredients = recipe.getIngredients().stream().map(
                ingredient -> this.readIngredientUseCase.execute(ingredient.getId())
        ).collect(Collectors.toSet());

        Recipe toUpdateRecipe = currentRecipe.toBuilder()
                .name(recipe.getName())
                .description(recipe.getDescription())
                .type(recipe.getType())
                .numberOfServings(recipe.getNumberOfServings())
                .ingredients(ingredients)
                .build();

        return this.recipeRepository.save(toUpdateRecipe);
    }
}
