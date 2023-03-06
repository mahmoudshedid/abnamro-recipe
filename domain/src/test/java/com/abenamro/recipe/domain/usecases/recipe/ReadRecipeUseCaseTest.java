package com.abenamro.recipe.domain.usecases.recipe;

import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.exception.ResourceNotFoundException;
import com.abnamro.recipe.domain.filter.PageFilters;
import com.abnamro.recipe.domain.filter.recipe.RecipeFilters;
import com.abnamro.recipe.domain.filter.recipe.RecipeSortFilters;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import com.abnamro.recipe.domain.usecases.recipe.ReadRecipeUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadRecipeUseCaseTest {

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

    private static final Recipe RECIPE_01 = Recipe.builder()
            .id(123L)
            .name("Name")
            .description("Description")
            .type(ERecipeType.VEGETARIAN)
            .numberOfServings(5)
            .ingredients(Set.of(INGREDIENT_EXIST_01, INGREDIENT_EXIST_02))
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Recipe RECIPE_02 = Recipe.builder()
            .id(124L)
            .name("Fake Name")
            .description("Fake Description")
            .type(ERecipeType.MEAT)
            .numberOfServings(4)
            .ingredients(Set.of(INGREDIENT_EXIST_01, INGREDIENT_EXIST_02, INGREDIENT_EXIST_03))
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    private static final RecipeFilters RECIPE_FILTERS = RecipeFilters.builder().build();

    private static final RecipeSortFilters RECIPE_SORT_FILTERS = RecipeSortFilters.builder().build();

    private static final PageFilters PAGE_FILTERS = PageFilters.builder().build();

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private ReadRecipeUseCase readRecipeUseCase;

    @Test
    public void readRecipe_success() {

        when(recipeRepository.findById(123L)).thenReturn(Optional.of(RECIPE_01));

        Recipe recipe = readRecipeUseCase.execute(123L);

        assertEquals(RECIPE_01, recipe);
        verify(recipeRepository).findById(123L);
    }

    @Test
    public void recipe_notExist() {

        when(recipeRepository.findById(125L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> readRecipeUseCase.execute(125L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(recipeRepository).findById(125L);

    }

    @Test
    public void readRecipe_success_page() {

        Pageable pageable = PageRequest.of(
                PAGE_FILTERS.getPage() - 1,
                PAGE_FILTERS.getSize(),
                Sort.Direction.fromString(RECIPE_SORT_FILTERS.getDirection().toString()),
                RECIPE_SORT_FILTERS.getFields().toString().toLowerCase(Locale.ROOT)
        );

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Recipe> recipeList = List.of(RECIPE_01, RECIPE_02);
        Page<Recipe> recipePage = new PageImpl<>(recipeList, pageRequest, recipeList.size());

        when(recipeRepository.findAll(
                pageable, RECIPE_FILTERS.getName(),
                RECIPE_FILTERS.getDescription(),
                null,
                RECIPE_FILTERS.getNumberOfServings()
        )).thenReturn(recipePage);

        Page<Recipe> recipe = readRecipeUseCase.execute(RECIPE_FILTERS, RECIPE_SORT_FILTERS, PAGE_FILTERS);

        assertEquals(recipePage, recipe);
        assertEquals(recipePage.getSize(), 10);
        assertEquals(recipePage.getTotalPages(), 1);
        assertEquals(recipePage.getContent().size(), 2);
        verify(recipeRepository).findAll(pageable, RECIPE_FILTERS.getName(),
                RECIPE_FILTERS.getDescription(),
                null,
                RECIPE_FILTERS.getNumberOfServings());
    }

    @Test
    public void readRecipe_success_page_withFilter() {

        RecipeFilters recipeFilters = RECIPE_FILTERS.toBuilder()
                .name("Name")
                .build();

        Pageable pageable = PageRequest.of(
                PAGE_FILTERS.getPage() - 1,
                PAGE_FILTERS.getSize(),
                Sort.Direction.fromString(RECIPE_SORT_FILTERS.getDirection().toString()),
                RECIPE_SORT_FILTERS.getFields().toString().toLowerCase(Locale.ROOT)
        );

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Recipe> recipeList = List.of(RECIPE_01);
        Page<Recipe> recipePage = new PageImpl<>(recipeList, pageRequest, recipeList.size());

        when(recipeRepository.findAll(
                pageable,
                recipeFilters.getName(),
                recipeFilters.getDescription(),
                null,
                recipeFilters.getNumberOfServings()
        )).thenReturn(recipePage);

        Page<Recipe> recipe = readRecipeUseCase.execute(recipeFilters, RECIPE_SORT_FILTERS, PAGE_FILTERS);

        assertEquals(recipePage, recipe);
        assertEquals(recipePage.getSize(), 10);
        assertEquals(recipePage.getTotalPages(), 1);
        assertEquals(recipePage.getContent().size(), 1);
        verify(recipeRepository).findAll(
                pageable,
                recipeFilters.getName(),
                recipeFilters.getDescription(),
                null,
                recipeFilters.getNumberOfServings()
        );
    }
}
