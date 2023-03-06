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
public class CreateRecipeUseCase {

    private final RecipeRepository recipeRepository;
    private final ReadIngredientUseCase readIngredientUseCase;

    public Recipe execute(Recipe recipe) {
        if (recipe == null) {
            throw new IllegalArgumentException("Recipe cannot be null");
        }

        Set<Ingredient> ingredients = recipe.getIngredients().stream().map(
                ingredient -> this.readIngredientUseCase.execute(ingredient.getId())
        ).collect(Collectors.toSet());

        Recipe recipeToCreate = recipe.toBuilder()
                .updatedBy(recipe.getCreatedBy())
                .ingredients(ingredients)
                .build();

        return this.recipeRepository.save(recipeToCreate);
    }
}
