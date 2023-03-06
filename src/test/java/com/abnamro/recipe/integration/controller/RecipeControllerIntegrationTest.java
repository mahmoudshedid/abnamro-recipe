package com.abnamro.recipe.integration.controller;

import com.abnamro.recipe.api.payloads.request.recipe.RecipeCreateRequest;
import com.abnamro.recipe.api.payloads.request.recipe.RecipeUpdateRequest;
import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RecipeControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    protected MockMvc mockMvc;

    @Before
    public void before() {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    @Test
    public void getRecipe_successfully() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        Recipe recipe = Recipe.builder()
                .name("Fake Name")
                .description("Fake Description")
                .type(ERecipeType.VEGETARIAN)
                .numberOfServings(5)
                .ingredients(Set.of(Ingredient.builder().id(savedIngredient.getId()).build()))
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Recipe savedRecipe = recipeRepository.save(recipe);

        ResultActions resultActions = performGet("/api/v1/recipe/" + savedRecipe.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(savedRecipe.getId()))
                .andExpect(jsonPath("$.data.name").value(recipe.getName()))
                .andExpect(jsonPath("$.data.createdBy").value(recipe.getCreatedBy()))
                .andExpect(jsonPath("$.data.updatedBy").value(recipe.getUpdatedBy()));
    }

    @Test
    public void getRecipe_notFound() throws Exception {
        performGet("/api/v1/recipe/1000")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void fetchRecipes_successfully() throws Exception {

        Ingredient ingredient = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        Recipe recipe01 = Recipe.builder()
                .name("Fake Name")
                .description("Fake Description")
                .type(ERecipeType.MEAT)
                .numberOfServings(5)
                .ingredients(Set.of(Ingredient.builder().id(savedIngredient.getId()).build()))
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Recipe recipe02 = Recipe.builder()
                .name("Name")
                .description("Description")
                .type(ERecipeType.VEGETARIAN)
                .numberOfServings(3)
                .ingredients(Set.of(Ingredient.builder().id(savedIngredient.getId()).build()))
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Recipe savedRecipe01 = recipeRepository.save(recipe01);
        Recipe savedRecipe02 = recipeRepository.save(recipe02);

        ResultActions resultActions = performGet("/api/v1/recipe/fetch/all?orderDirection=DESC&orderField=ID&page=1&size=10");
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value(savedRecipe02.getName()))
                .andExpect(jsonPath("$.data[1].name").value(savedRecipe01.getName()));
    }

    @Test
    public void createRecipe_successfully() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        RecipeCreateRequest request = new RecipeCreateRequest();
        request.setName("Fake Name");
        request.setDescription("Fake Description");
        request.setType("MEAT");
        request.setNumberOfServings(5);
        request.setIngredients(Set.of(savedIngredient.getId()));
        request.setCreatedBy("fakeemail@fakeserver.com");

        MvcResult result = performPost("/api/v1/recipe", request)
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readByJsonPath(result, "$.data.id");

        Optional<Recipe> recipe = recipeRepository.findById(id);

        assertTrue(recipe.isPresent());
        assertEquals(recipe.get().getName(), request.getName());
    }

    @Test
    public void updateRecipe_successfully() throws Exception {

        Ingredient ingredient01 = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Ingredient ingredient02 = Ingredient.builder()
                .name("Name")
                .description("Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Ingredient savedIngredient01 = ingredientRepository.save(ingredient01);
        Ingredient savedIngredient02 = ingredientRepository.save(ingredient02);

        Recipe recipeExisted = Recipe.builder()
                .name("Fake Name")
                .description("Fake Description")
                .type(ERecipeType.VEGETARIAN)
                .numberOfServings(3)
                .ingredients(Set.of(Ingredient.builder().id(savedIngredient01.getId()).build()))
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Recipe savedRecipe = recipeRepository.save(recipeExisted);

        RecipeUpdateRequest request = new RecipeUpdateRequest();
        request.setId(savedRecipe.getId());
        request.setName("Name");
        request.setDescription("Description");
        request.setType("VEGETARIAN");
        request.setNumberOfServings(4);
        request.setIngredients(Set.of(savedIngredient02.getId()));
        request.setUpdatedBy("fakeemail01@fakeserver.com");

        MvcResult result = performPatch("/api/v1/recipe", request)
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Integer id = readByJsonPath(result, "$.data.id");

        Optional<Recipe> recipe = recipeRepository.findById(id);

        assertTrue(recipe.isPresent());
        assertEquals(recipe.get().getName(), request.getName());
    }

    @Test
    public void deleteRecipes_successfully() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        Recipe recipe = Recipe.builder()
                .name("Fake Name")
                .description("Fake Description")
                .type(ERecipeType.MEAT)
                .numberOfServings(5)
                .ingredients(Set.of(Ingredient.builder().id(savedIngredient.getId()).build()))
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();
        Recipe savedRecipe = recipeRepository.save(recipe);

        performDelete("/api/v1/recipe/" + savedRecipe.getId())
                .andExpect(status().isOk());

        Optional<Recipe> deletedRecipe = recipeRepository.findById(savedRecipe.getId());
        assertTrue(deletedRecipe.isEmpty());
    }

    @Test
    public void deleteRecipe_notFound() throws Exception {

        performDelete("/api/v1/recipe/1100")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
