package com.abnamro.recipe.domain.usecases.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.exception.ResourceNotFoundException;
import com.abnamro.recipe.domain.filter.PageFilters;
import com.abnamro.recipe.domain.filter.ingredient.IngredientFilters;
import com.abnamro.recipe.domain.filter.ingredient.IngredientSortFilters;
import com.abnamro.recipe.domain.repository.IngredientRepository;
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
public class ReadIngredientUseCase {

    private final IngredientRepository ingredientRepository;

    public Ingredient execute(long id) {
        return ingredientRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                MessageFormat.format("Ingredient not found by id {0}", id)
        ));
    }

    public Page<Ingredient> execute(IngredientFilters filters, IngredientSortFilters sortFilters, PageFilters pageFilters) {

        Pageable pageable = PageRequest.of(
                pageFilters.getPage() - 1,
                pageFilters.getSize(),
                Sort.Direction.fromString(sortFilters.getDirection().toString()),
                sortFilters.getFields().toString().toLowerCase(Locale.ROOT)
        );

        return this.ingredientRepository.findAll(pageable, filters.getName(), filters.getDescription());
    }
}
