package com.abnamro.recipe.domain.usecases.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdateIngredientUseCase {

    private final ReadIngredientUseCase readIngredientUseCase;
    private final IngredientRepository ingredientRepository;

    public Ingredient execute(Ingredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }
        Ingredient currentIngredient = this.readIngredientUseCase.execute(ingredient.getId());

        Ingredient toUpdateIngredient = currentIngredient.toBuilder()
                .name(ingredient.getName())
                .description(ingredient.getDescription())
                .build();

        return this.ingredientRepository.save(toUpdateIngredient);
    }
}
