package com.abenamro.recipe.domain.usecases.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import com.abnamro.recipe.domain.usecases.ingredient.ReadIngredientUseCase;
import com.abnamro.recipe.domain.usecases.ingredient.UpdateIngredientUseCase;
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
public class UpdateIngredientUseCaseTest {

    private static final Ingredient INGREDIENT_EXIST = Ingredient.builder()
            .id(123L)
            .name("Fake Name")
            .description("Fake Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Ingredient INGREDIENT_UPDATED = Ingredient.builder()
            .id(123L)
            .name("Fake Name Second")
            .description("Fake Description Second")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    private static final Ingredient INGREDIENT = Ingredient.builder()
            .id(123L)
            .name("Fake Name Second")
            .description("Fake Description Second")
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    @Mock
    private ReadIngredientUseCase readIngredientUseCase;
    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private UpdateIngredientUseCase updateIngredientUseCase;

    @Test
    public void updateIngredient_success() {

        when(readIngredientUseCase.execute(123L)).thenReturn(INGREDIENT_EXIST);

        when(ingredientRepository.save(any(Ingredient.class))).thenReturn(INGREDIENT_UPDATED);

        Ingredient ingredient = updateIngredientUseCase.execute(INGREDIENT);

        assertEquals(INGREDIENT_UPDATED, ingredient);
        verify(readIngredientUseCase).execute(123L);
        verify(ingredientRepository).save(any(Ingredient.class));

    }

    @Test
    public void updateIngredient_notExist() {

        assertThatThrownBy(() -> updateIngredientUseCase.execute(INGREDIENT))
                .isInstanceOf(NullPointerException.class);

        verify(readIngredientUseCase).execute(123L);
        verifyNoInteractions(ingredientRepository);

    }

    @Test
    public void updateIngredient_nullable() {
        assertThatThrownBy(() -> updateIngredientUseCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Ingredient cannot be null");

        verifyNoInteractions(readIngredientUseCase);
        verifyNoInteractions(ingredientRepository);

    }
}
