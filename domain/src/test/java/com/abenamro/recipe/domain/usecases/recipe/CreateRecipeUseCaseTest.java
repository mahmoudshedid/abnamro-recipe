package com.abenamro.recipe.domain.usecases.recipe;

import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import com.abnamro.recipe.domain.usecases.ingredient.ReadIngredientUseCase;
import com.abnamro.recipe.domain.usecases.recipe.CreateRecipeUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateRecipeUseCaseTest {

    private static final Ingredient INGREDIENT_EXIST_01 = Ingredient.builder()
            .id(123L)
            .name("Fake Name One")
            .description("Fake Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Ingredient INGREDIENT_EXIST_02 = Ingredient.builder()
            .id(124L)
            .name("Fake Name Two")
            .description("Fake Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Recipe RECIPE = Recipe.builder()
            .name("Name")
            .description("Description")
            .type(ERecipeType.VEGETARIAN)
            .numberOfServings(5)
            .ingredients(Set.of(INGREDIENT_EXIST_01, INGREDIENT_EXIST_02))
            .createdBy("fakeemail@fakeserver.com")
            .build();

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    ReadIngredientUseCase readIngredientUseCase;

    @InjectMocks
    private CreateRecipeUseCase createRecipeUseCase;

    @Test
    public void createRecipe_success() {

        Recipe created = RECIPE.toBuilder()
                .id(123L)
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        when(readIngredientUseCase.execute(eq(123L))).thenReturn(INGREDIENT_EXIST_01);
        when(readIngredientUseCase.execute(eq(124L))).thenReturn(INGREDIENT_EXIST_02);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(created);

        Recipe recipe = createRecipeUseCase.execute(RECIPE);

        assertEquals(created, recipe);
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    public void createRecipe_nullable() {
        assertThatThrownBy(() -> createRecipeUseCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Recipe cannot be null");

        verifyNoInteractions(recipeRepository);

    }
}
