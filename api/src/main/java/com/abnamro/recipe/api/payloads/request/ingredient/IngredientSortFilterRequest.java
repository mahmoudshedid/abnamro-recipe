package com.abnamro.recipe.api.payloads.request.ingredient;

import com.abnamro.recipe.api.validations.annotations.ValidEnumValue;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.filter.OrderDirection;
import com.abnamro.recipe.domain.filter.ingredient.IngredientSortFilters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientSortFilterRequest {

    @ApiModelProperty(notes = "Sort Ingredients by fields", example = "ID")
    @ValidEnumValue(required = true, enumClass = Ingredient.SortingFields.class)
    private String orderField = Ingredient.SortingFields.ID.name();

    @ApiModelProperty(notes = "Sort Ingredients by direction", example = "DESC")
    @ValidEnumValue(required = true, enumClass = OrderDirection.class)
    private String orderDirection = OrderDirection.DESC.name();

    public IngredientSortFilters toEntity() {
        return IngredientSortFilters.builder()
                .fields(Ingredient.SortingFields.valueOf(orderField))
                .direction(OrderDirection.valueOf(orderDirection))
                .build();
    }
}
