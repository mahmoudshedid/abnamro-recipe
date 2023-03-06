package com.abnamro.recipe.api.controllers;

import com.abnamro.recipe.api.payloads.response.ApiResponsePayload;
import com.abnamro.recipe.api.payloads.response.recipe.RecipeResponse;
import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.filter.PageFilters;
import com.abnamro.recipe.domain.filter.recipe.RecipeFilters;
import com.abnamro.recipe.domain.filter.recipe.RecipeSortFilters;
import com.abnamro.recipe.domain.usecases.recipe.CreateRecipeUseCase;
import com.abnamro.recipe.domain.usecases.recipe.DeleteRecipeUseCase;
import com.abnamro.recipe.domain.usecases.recipe.ReadRecipeUseCase;
import com.abnamro.recipe.domain.usecases.recipe.UpdateRecipeUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(RecipeRestController.class)
public class RecipeControllerTest extends BaseControllerTest {

    private static final String BASE_ENDPOINT = "/api/v1/recipe";

    private static final Ingredient VALID_INGREDIENT = Ingredient.builder()
            .id(123L)
            .name("Fake Name")
            .description("Fake Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Ingredient VALID_INGREDIENT_01 = Ingredient.builder()
            .id(124L)
            .name("Fake Name Second")
            .description("Fake Description Second")
            .createdBy("fakeemail01@fakeserver.com")
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    private static final Ingredient VALID_INGREDIENT_03 = Ingredient.builder()
            .id(125L)
            .name("Fake Name Third")
            .description("Fake Description Third")
            .createdBy("fakeemail03@fakeserver.com")
            .updatedBy("fakeemail03@fakeserver.com")
            .build();

    private static final Recipe VALID_RECIPE = Recipe.builder()
            .id(126L)
            .name("Fake Name")
            .description("Fake Description")
            .type(ERecipeType.VEGETARIAN)
            .numberOfServings(5)
            .ingredients(Set.of(VALID_INGREDIENT, VALID_INGREDIENT_01))
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Recipe VALID_RECIPE_01 = Recipe.builder()
            .id(123L)
            .name("Fake Name Second")
            .description("Fake Description Second")
            .numberOfServings(4)
            .type(ERecipeType.MEAT)
            .createdBy("fakeemail01@fakeserver.com")
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    private static final Long RECIPE_ID = 123L;

    @MockBean
    private ReadRecipeUseCase readRecipeUseCase;
    @MockBean
    private CreateRecipeUseCase createRecipeUseCase;
    @MockBean
    private UpdateRecipeUseCase updateRecipeUseCase;
    @MockBean
    private DeleteRecipeUseCase deleteRecipeUseCase;

    @Test
    public void getRecipe_success() throws Exception {

        when(readRecipeUseCase.execute(RECIPE_ID)).thenReturn(VALID_RECIPE);

        ApiResponsePayload<RecipeResponse> response = request(
                get(BASE_ENDPOINT + "/" + RECIPE_ID),
                ApiResponsePayload.class
        );

        ObjectMapper mapper = new ObjectMapper();
        RecipeResponse recipeResponse = mapper.convertValue(response.getData(), RecipeResponse.class);

        assertThat(response).isNotNull();
        assertThat(recipeResponse.getName()).isEqualTo("Fake Name");
        assertThat(recipeResponse.getUpdatedBy()).isEqualTo("fakeemail@fakeserver.com");
        verify(readRecipeUseCase).execute(RECIPE_ID);
    }

    @Test
    public void fetchAllRecipe_byFilters_success() throws Exception {

        RecipeFilters recipeFilters = RecipeFilters.builder().build();
        RecipeSortFilters recipeSortFilters = RecipeSortFilters.builder().build();
        PageFilters pageFilters = PageFilters.builder().build();

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Recipe> recipeList = List.of(VALID_RECIPE, VALID_RECIPE_01);
        Page<Recipe> recipePage = new PageImpl<>(recipeList, pageRequest, recipeList.size());

        when(readRecipeUseCase.execute(recipeFilters, recipeSortFilters, pageFilters)).thenReturn(recipePage);

        MockHttpServletResponse response = request(get(BASE_ENDPOINT + "/fetch/all"), HttpStatus.OK).andReturn().getResponse();

        assertThat(response).isNotNull();
        verify(readRecipeUseCase).execute(recipeFilters, recipeSortFilters, pageFilters);
    }

    @Test
    public void createRecipe_success() throws Exception {
        String payload = "recipe/valid_recipe_values.json";

        Recipe recipe = Recipe.builder()
                .name("Fake Name")
                .description("Fake Description")
                .type(ERecipeType.VEGETARIAN)
                .numberOfServings(5)
                .ingredients(Set.of(Ingredient.builder().id(123L).build(), Ingredient.builder().id(124L).build()))
                .createdBy("fakeemail@fakeserver.com")
                .build();

        when(createRecipeUseCase.execute(recipe)).thenReturn(VALID_RECIPE);

        ApiResponsePayload<RecipeResponse> response = request(
                post(BASE_ENDPOINT),
                payload,
                HttpStatus.CREATED,
                ApiResponsePayload.class
        );

        ObjectMapper mapper = new ObjectMapper();
        RecipeResponse recipeResponse = mapper.convertValue(response.getData(), RecipeResponse.class);

        assertThat(response).isNotNull();
        assertThat(recipeResponse.getName()).isEqualTo("Fake Name");
        assertThat(recipeResponse.getUpdatedBy()).isEqualTo("fakeemail@fakeserver.com");
        verify(createRecipeUseCase).execute(recipe);
    }

    @Test
    public void createRecipe_InvalidRequest() throws Exception {
        String payload = "recipe/invalid_recipe_values.json";

        HttpMessageNotReadableException response = request(
                post(BASE_ENDPOINT),
                payload,
                HttpStatus.BAD_REQUEST,
                HttpMessageNotReadableException.class
        );

        assertThat(response.getMessage()).isEqualTo("Recipe Name can't be blank");
        verifyNoInteractions(createRecipeUseCase);
    }

    @Test
    public void updateRecipe_success() throws Exception {
        String payload = "recipe/valid_update_recipe_values.json";

        Recipe recipe = Recipe.builder()
                .id(123L)
                .name("Fake Name Second")
                .description("Fake Description Second")
                .type(ERecipeType.MEAT)
                .numberOfServings(4)
                .ingredients(Set.of(
                                Ingredient.builder().id(123L).build(),
                                Ingredient.builder().id(124L).build(),
                                Ingredient.builder().id(125L).build()
                        )
                )
                .updatedBy("fakeemail01@fakeserver.com")
                .build();

        when(updateRecipeUseCase.execute(recipe)).thenReturn(VALID_RECIPE_01);

        ApiResponsePayload<RecipeResponse> response = request(
                patch(BASE_ENDPOINT),
                payload,
                HttpStatus.OK,
                ApiResponsePayload.class
        );

        ObjectMapper mapper = new ObjectMapper();
        RecipeResponse recipeResponse = mapper.convertValue(response.getData(), RecipeResponse.class);

        assertThat(response).isNotNull();
        assertThat(recipeResponse.getName()).isEqualTo("Fake Name Second");
        assertThat(recipeResponse.getUpdatedBy()).isEqualTo("fakeemail01@fakeserver.com");
        verify(updateRecipeUseCase).execute(recipe);
    }

    @Test
    public void deleteRecipe_success() throws Exception {

        request(
                delete(BASE_ENDPOINT + "/" + RECIPE_ID),
                HttpStatus.OK
        );

        verify(deleteRecipeUseCase).execute(RECIPE_ID);
    }

}