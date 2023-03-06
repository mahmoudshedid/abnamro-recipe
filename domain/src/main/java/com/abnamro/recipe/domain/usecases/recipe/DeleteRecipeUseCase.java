package com.abnamro.recipe.domain.usecases.recipe;

import com.abnamro.recipe.domain.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteRecipeUseCase {

    private final RecipeRepository recipeRepository;
    private final ReadRecipeUseCase readRecipeUseCase;

    public void execute(long id) {
        this.readRecipeUseCase.execute(id);

        this.recipeRepository.delete(id);
    }
}
