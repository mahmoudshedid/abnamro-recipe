package com.abenamro.recipe.domain.usecases.recipe;

import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import com.abnamro.recipe.domain.usecases.recipe.DeleteRecipeUseCase;
import com.abnamro.recipe.domain.usecases.recipe.ReadRecipeUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeleteRecipeUseCaseTest {

    private static final Recipe RECIPE_01 = Recipe.builder()
            .id(123L)
            .name("Name")
            .description("Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    private ReadRecipeUseCase readRecipeUseCase;

    @InjectMocks
    private DeleteRecipeUseCase deleteRecipeUseCase;

    @Test
    public void deleteRecipe_success() {
        when(readRecipeUseCase.execute(123L)).thenReturn(RECIPE_01);

        deleteRecipeUseCase.execute(123L);

        verify(readRecipeUseCase).execute(123L);
        verify(recipeRepository).delete(123L);
    }

    @Test
    public void deleteRecipe_notExist() {

        when(readRecipeUseCase.execute(125L)).thenThrow(NullPointerException.class);

        assertThatThrownBy(() -> deleteRecipeUseCase.execute(125L))
                .isInstanceOf(NullPointerException.class);

        verify(readRecipeUseCase).execute(125L);
        verifyNoInteractions(recipeRepository);

    }
}
