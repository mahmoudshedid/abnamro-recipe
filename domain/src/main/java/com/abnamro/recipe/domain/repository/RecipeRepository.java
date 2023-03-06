package com.abnamro.recipe.domain.repository;

import com.abnamro.recipe.domain.entities.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RecipeRepository {

    Optional<Recipe> findById(long id);

    Page<Recipe> findAll(Pageable pageable, String name, String description, String type, int numberOfServings);

    Recipe save(Recipe recipe);

    void delete(long id);

    void deleteAll();
}
