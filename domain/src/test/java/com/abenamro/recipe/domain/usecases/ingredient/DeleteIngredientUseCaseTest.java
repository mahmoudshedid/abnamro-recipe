package com.abenamro.recipe.domain.usecases.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import com.abnamro.recipe.domain.usecases.ingredient.DeleteIngredientUseCase;
import com.abnamro.recipe.domain.usecases.ingredient.ReadIngredientUseCase;
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
public class DeleteIngredientUseCaseTest {

    private static final Ingredient INGREDIENT_01 = Ingredient.builder()
            .id(123L)
            .name("Name")
            .description("Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    @Mock
    private IngredientRepository ingredientRepository;
    @Mock
    private ReadIngredientUseCase readIngredientUseCase;

    @InjectMocks
    private DeleteIngredientUseCase deleteIngredientUseCase;

    @Test
    public void deleteIngredient_success() {
        when(readIngredientUseCase.execute(123L)).thenReturn(INGREDIENT_01);

        deleteIngredientUseCase.execute(123L);

        verify(readIngredientUseCase).execute(123L);
        verify(ingredientRepository).delete(123L);
    }

    @Test
    public void deleteIngredient_notExist() {

        when(readIngredientUseCase.execute(125L)).thenThrow(NullPointerException.class);

        assertThatThrownBy(() -> deleteIngredientUseCase.execute(125L))
                .isInstanceOf(NullPointerException.class);

        verify(readIngredientUseCase).execute(125L);
        verifyNoInteractions(ingredientRepository);

    }
}
