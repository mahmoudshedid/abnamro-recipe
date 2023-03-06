package com.abnamro.recipe.domain.repository;

import com.abnamro.recipe.domain.entities.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface IngredientRepository {

    Optional<Ingredient> findById(long id);

    Page<Ingredient> findAll(Pageable pageable, String name, String description);

    Ingredient save(Ingredient recipe);

    void delete(long id);

    void deleteAll();
}
