package com.abnamro.recipe.domain.usecases.recipe;

import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.exception.ResourceNotFoundException;
import com.abnamro.recipe.domain.filter.PageFilters;
import com.abnamro.recipe.domain.filter.recipe.RecipeFilters;
import com.abnamro.recipe.domain.filter.recipe.RecipeSortFilters;
import com.abnamro.recipe.domain.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ReadRecipeUseCase {

    private final RecipeRepository recipeRepository;

    public Recipe execute(long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                MessageFormat.format("Recipe not found by id {0}", id)
        ));
    }

    public Page<Recipe> execute(RecipeFilters filters, RecipeSortFilters sortFilters, PageFilters pageFilters) {

        Pageable pageable = PageRequest.of(
                pageFilters.getPage() - 1,
                pageFilters.getSize(),
                Sort.Direction.fromString(sortFilters.getDirection().toString()),
                sortFilters.getFields().toString().toLowerCase(Locale.ROOT)
        );

        String recipeType = filters.getType() != null ? filters.getType().toString() : null;

        return this.recipeRepository.findAll(
                pageable, filters.getName(), filters.getDescription(), recipeType, filters.getNumberOfServings()
        );
    }
}
