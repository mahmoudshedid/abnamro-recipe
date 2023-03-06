package com.abnamro.recipe.domain.usecases.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateIngredientUseCase {

    private final IngredientRepository ingredientRepository;

    public Ingredient execute(Ingredient ingredient) {
        if (ingredient == null) {
            throw new IllegalArgumentException("Ingredient cannot be null");
        }

        Ingredient ingredientToCreate = ingredient.toBuilder()
                .updatedBy(ingredient.getCreatedBy())
                .build();

        return this.ingredientRepository.save(ingredientToCreate);
    }
}
