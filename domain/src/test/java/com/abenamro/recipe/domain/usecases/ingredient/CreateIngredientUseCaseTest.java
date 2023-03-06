package com.abenamro.recipe.domain.usecases.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import com.abnamro.recipe.domain.usecases.ingredient.CreateIngredientUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateIngredientUseCaseTest {

    private static final Ingredient INGREDIENT = Ingredient.builder()
            .name("Fake Name")
            .description("Fake Description")
            .createdBy("fakeemail@fakeserver.com")
            .build();

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private CreateIngredientUseCase createIngredientUseCase;

    @Test
    public void createIngredient_success() {

        Ingredient created = INGREDIENT.toBuilder()
                .id(123L)
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(created);

        Ingredient ingredient = createIngredientUseCase.execute(INGREDIENT);

        assertEquals(created, ingredient);
        verify(ingredientRepository).save(any(Ingredient.class));
    }

    @Test
    public void createIngredient_nullable() {
        assertThatThrownBy(() -> createIngredientUseCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ingredient cannot be null");

        verifyNoInteractions(ingredientRepository);

    }
}
