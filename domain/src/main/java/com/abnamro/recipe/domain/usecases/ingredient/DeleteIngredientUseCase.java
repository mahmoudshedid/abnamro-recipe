package com.abnamro.recipe.domain.usecases.ingredient;

import com.abnamro.recipe.domain.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteIngredientUseCase {

    private final IngredientRepository ingredientRepository;
    private final ReadIngredientUseCase readIngredientUseCase;

    public void execute(long id) {
        this.readIngredientUseCase.execute(id);

        this.ingredientRepository.delete(id);
    }
}
