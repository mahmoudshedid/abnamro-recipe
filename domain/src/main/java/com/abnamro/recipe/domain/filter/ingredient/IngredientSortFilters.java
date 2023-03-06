package com.abnamro.recipe.domain.filter.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.filter.OrderDirection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
public class IngredientSortFilters {
    @Builder.Default
    private Ingredient.SortingFields fields = Ingredient.SortingFields.ID;
    @Builder.Default
    private OrderDirection direction = OrderDirection.DESC;
}
