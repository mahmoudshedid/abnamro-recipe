package com.abnamro.recipe.integration.controller;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IngredientControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    protected MockMvc mockMvc;

    @Before
    public void before() {
        ingredientRepository.deleteAll();
    }

    @Test
    public void getIngredient_successfully() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        ResultActions resultActions = performGet("/api/v1/ingredient/" + savedIngredient.getId());
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(savedIngredient.getId()))
                .andExpect(jsonPath("$.data.name").value(ingredient.getName()))
                .andExpect(jsonPath("$.data.createdBy").value(ingredient.getCreatedBy()))
                .andExpect(jsonPath("$.data.updatedBy").value(ingredient.getUpdatedBy()));
    }

    @Test
    public void getIngredient_notFound() throws Exception {
        performGet("/api/v1/ingredient/1000")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void fetchIngredients_successfully() throws Exception {

        Ingredient ingredient01 = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Ingredient ingredient02 = Ingredient.builder()
                .name("Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Ingredient savedIngredient01 = ingredientRepository.save(ingredient01);
        Ingredient savedIngredient02 = ingredientRepository.save(ingredient02);

        ResultActions resultActions = performGet("/api/v1/ingredient/fetch/all?orderDirection=DESC&orderField=ID&page=1&size=10");
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].name").value(savedIngredient02.getName()))
                .andExpect(jsonPath("$.data[1].name").value(savedIngredient01.getName()));
    }

    @Test
    public void createIngredient_successfully() throws Exception {
        Ingredient request = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .build();

        MvcResult result = performPost("/api/v1/ingredient", request)
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readByJsonPath(result, "$.data.id");

        Optional<Ingredient> ingredient = ingredientRepository.findById(id);

        assertTrue(ingredient.isPresent());
        assertEquals(ingredient.get().getName(), request.getName());
    }

    @Test
    public void updateIngredient_successfully() throws Exception {
        Ingredient ingredientExisted = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();

        Ingredient savedIngredient = ingredientRepository.save(ingredientExisted);

        Ingredient request = Ingredient.builder()
                .id(savedIngredient.getId())
                .name("Name")
                .description("Description")
                .updatedBy("fakeemail01@fakeserver.com")
                .build();

        MvcResult result = performPatch("/api/v1/ingredient", request)
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Integer id = readByJsonPath(result, "$.data.id");

        Optional<Ingredient> ingredient = ingredientRepository.findById(id);

        assertTrue(ingredient.isPresent());
        assertEquals(ingredient.get().getName(), request.getName());
    }

    @Test
    public void deleteIngredients_successfully() throws Exception {
        Ingredient ingredient = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .updatedBy("fakeemail@fakeserver.com")
                .build();
        Ingredient savedIngredient = ingredientRepository.save(ingredient);

        performDelete("/api/v1/ingredient/" + savedIngredient.getId())
                .andExpect(status().isOk());

        Optional<Ingredient> deletedIngredient = ingredientRepository.findById(savedIngredient.getId());
        assertTrue(deletedIngredient.isEmpty());
    }

    @Test
    public void deleteIngredient_notFound() throws Exception {

        performDelete("/api/v1/ingredient/1100")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
