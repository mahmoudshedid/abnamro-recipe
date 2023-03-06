package com.abnamro.recipe.data.repository;

import com.abnamro.recipe.data.entites.IngredientData;
import com.abnamro.recipe.data.entites.RecipeData;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Repository
@RequiredArgsConstructor
public class RecipeRepositoryData implements RecipeRepository {

    private final RecipeRepositoryDataJpa recipeRepositoryDataJpa;

    @Override
    @Transactional(readOnly = true)
    public Optional<Recipe> findById(long id) {
        return recipeRepositoryDataJpa.findById(id).map(RecipeData::toEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Recipe> findAll(Pageable pageable, String name, String description, String type, int numberOfServings) {
        return recipeRepositoryDataJpa.findAll(name, description, type, numberOfServings, pageable).map(RecipeData::toEntity);
    }

    @Override
    public Recipe save(Recipe recipe) {
        return recipeRepositoryDataJpa.save(
                RecipeData.builder()
                        .id(recipe.getId())
                        .name(recipe.getName())
                        .description(recipe.getDescription())
                        .type(recipe.getType() != null ? recipe.getType().toString() : null)
                        .numberOfServings(recipe.getNumberOfServings())
                        .ingredients(recipe.getIngredients() != null
                                ? recipe.getIngredients().stream().map(IngredientData::new).collect(Collectors.toSet())
                                : null)
                        .createdBy(recipe.getCreatedBy())
                        .createdAt(recipe.getCreatedAt())
                        .updatedBy(recipe.getUpdatedBy())
                        .updatedAt(recipe.getUpdatedAt())
                        .build()
        ).toEntity();
    }

    @Override
    public void delete(long id) {
        this.recipeRepositoryDataJpa.deleteById(id);
    }

    @Override
    public void deleteAll() {
        this.recipeRepositoryDataJpa.deleteAll();
    }
}
