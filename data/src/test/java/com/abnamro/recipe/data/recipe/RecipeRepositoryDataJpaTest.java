package com.abnamro.recipe.data.recipe;

import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RecipeRepositoryDataJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void testFindById() {

        Ingredient ingredient = getIngredientData();

        ingredientRepository.save(ingredient);

        Recipe recipe = getRecipeData();

        Recipe inserted = recipeRepository.save(recipe);
        Recipe result = recipeRepository.findById(inserted.getId()).get();
        assertEquals(recipe.getName(), result.getName());
    }

    @Test
    public void testFindAll() {

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.fromString("DESC"), "id");

        Ingredient ingredient = getIngredientData();

        ingredientRepository.save(ingredient);

        Recipe recipe = getRecipeData();

        recipeRepository.save(recipe);

        List<Recipe> result = new ArrayList<>();
        recipeRepository.findAll(pageable, null, null, null, 0).forEach(e -> result.add(e));

        assertEquals(result.size(), 1);
    }

    @Test
    public void testSave() {
        Ingredient ingredient = getIngredientData();

        ingredientRepository.save(ingredient);

        Recipe recipe = getRecipeData();

        Recipe inserted = recipeRepository.save(recipe);
        Recipe result = recipeRepository.findById(inserted.getId()).get();
        assertEquals(recipe.getName(), result.getName());
    }

    @Test
    public void testDeleteById() {

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.fromString("DESC"), "id");

        Ingredient ingredient = getIngredientData();

        ingredientRepository.save(ingredient);

        Recipe recipe = getRecipeData();

        Recipe inserted = recipeRepository.save(recipe);
        recipeRepository.delete(inserted.getId());
        List<Recipe> result = new ArrayList<>();

        recipeRepository.findAll(pageable, null, null, null, 0).forEach(e -> result.add(e));
        assertEquals(result.size(), 0);
    }

    private Recipe getRecipeData() {
        Recipe recipe = Recipe.builder().build();
        recipe.setName("Fake");
        recipe.setDescription("Fake Description");
        recipe.setType(ERecipeType.VEGETARIAN);
        recipe.setNumberOfServings(5);
        recipe.setIngredients(Set.of(getIngredientData()));
        recipe.setCreatedBy("fake@test.com");
        recipe.setUpdatedBy("fake@test.com");
        return recipe;
    }

    private Ingredient getIngredientData() {
        Ingredient ingredient = new Ingredient();
        ingredient.setName("Fake");
        ingredient.setDescription("Fake Description");
        ingredient.setCreatedBy("fake@test.com");
        ingredient.setUpdatedBy("fake@test.com");
        return ingredient;
    }
}
