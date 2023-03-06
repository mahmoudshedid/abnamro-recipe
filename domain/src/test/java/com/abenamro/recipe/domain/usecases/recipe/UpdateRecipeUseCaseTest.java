package com.abenamro.recipe.domain.usecases.recipe;

import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import com.abnamro.recipe.domain.usecases.ingredient.ReadIngredientUseCase;
import com.abnamro.recipe.domain.usecases.recipe.ReadRecipeUseCase;
import com.abnamro.recipe.domain.usecases.recipe.UpdateRecipeUseCase;
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
public class UpdateRecipeUseCaseTest {

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

    private static final Ingredient INGREDIENT_EXIST_03 = Ingredient.builder()
            .id(125L)
            .name("Fake Name Three")
            .description("Fake Description Three")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Recipe RECIPE_EXIST = Recipe.builder()
            .id(123L)
            .name("Fake Name")
            .description("Fake Description")
            .type(ERecipeType.VEGETARIAN)
            .numberOfServings(5)
            .ingredients(Set.of(INGREDIENT_EXIST_01, INGREDIENT_EXIST_02))
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Recipe RECIPE_UPDATED = Recipe.builder()
            .id(123L)
            .name("Fake Name Second")
            .description("Fake Description Second")
            .type(ERecipeType.MEAT)
            .numberOfServings(4)
            .ingredients(Set.of(INGREDIENT_EXIST_01, INGREDIENT_EXIST_02, INGREDIENT_EXIST_03))
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    private static final Recipe RECIPE = Recipe.builder()
            .id(123L)
            .name("Fake Name Second")
            .description("Fake Description Second")
            .type(ERecipeType.MEAT)
            .numberOfServings(4)
            .ingredients(Set.of(INGREDIENT_EXIST_01, INGREDIENT_EXIST_02, INGREDIENT_EXIST_03))
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    @Mock
    private ReadRecipeUseCase readRecipeUseCase;
    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    ReadIngredientUseCase readIngredientUseCase;

    @InjectMocks
    private UpdateRecipeUseCase updateRecipeUseCase;

    @Test
    public void updateRecipe_success() {

        when(readRecipeUseCase.execute(123L)).thenReturn(RECIPE_EXIST);
        when(readIngredientUseCase.execute(eq(123L))).thenReturn(INGREDIENT_EXIST_01);
        when(readIngredientUseCase.execute(eq(124L))).thenReturn(INGREDIENT_EXIST_02);
        when(readIngredientUseCase.execute(eq(125L))).thenReturn(INGREDIENT_EXIST_03);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(RECIPE_UPDATED);

        Recipe recipe = updateRecipeUseCase.execute(RECIPE);

        assertEquals(RECIPE_UPDATED, recipe);
        verify(readRecipeUseCase).execute(123L);
        verify(recipeRepository).save(any(Recipe.class));

    }

    @Test
    public void updateRecipe_notExist() {

        assertThatThrownBy(() -> updateRecipeUseCase.execute(RECIPE))
                .isInstanceOf(NullPointerException.class);

        verify(readRecipeUseCase).execute(123L);
        verifyNoInteractions(recipeRepository);

    }

    @Test
    public void updateRecipe_nullable() {
        assertThatThrownBy(() -> updateRecipeUseCase.execute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Recipe cannot be null");

        verifyNoInteractions(readRecipeUseCase);
        verifyNoInteractions(recipeRepository);

    }
}
