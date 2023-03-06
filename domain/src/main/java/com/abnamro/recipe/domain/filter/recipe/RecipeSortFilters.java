package com.abnamro.recipe.domain.filter.recipe;

import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.filter.OrderDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class RecipeSortFilters {
    @Builder.Default
    private Recipe.SortingFields fields = Recipe.SortingFields.ID;
    @Builder.Default
    private OrderDirection direction = OrderDirection.DESC;
}
