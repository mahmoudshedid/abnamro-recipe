package com.abnamro.recipe.data.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.repository.IngredientRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class IngredientRepositoryDataJpaTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void testFindById() {

        Ingredient ingredient = getIngredientData();

        Ingredient inserted = ingredientRepository.save(ingredient);
        Ingredient result = ingredientRepository.findById(inserted.getId()).get();
        assertEquals(ingredient.getName(), result.getName());
    }

    @Test
    public void testFindAll() {

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.fromString("DESC"), "id");

        Ingredient ingredient = getIngredientData();

        ingredientRepository.save(ingredient);

        List<Ingredient> result = new ArrayList<>();
        ingredientRepository.findAll(pageable, null, null).forEach(e -> result.add(e));

        assertEquals(result.size(), 1);
    }

    @Test
    public void testSave() {
        Ingredient ingredient = getIngredientData();

        Ingredient inserted = ingredientRepository.save(ingredient);
        Ingredient result = ingredientRepository.findById(inserted.getId()).get();
        assertEquals(ingredient.getName(), result.getName());
    }

    @Test
    public void testDeleteById() {

        Pageable pageable = PageRequest.of(0, 10, Sort.Direction.fromString("DESC"), "id");

        Ingredient ingredient = getIngredientData();

        Ingredient inserted = ingredientRepository.save(ingredient);
        ingredientRepository.delete(inserted.getId());
        List<Ingredient> result = new ArrayList<>();

        ingredientRepository.findAll(pageable, null, null).forEach(e -> result.add(e));
        assertEquals(result.size(), 0);
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
