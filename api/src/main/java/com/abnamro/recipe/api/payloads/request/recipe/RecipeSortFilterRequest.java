package com.abnamro.recipe.api.payloads.request.recipe;

import com.abnamro.recipe.api.validations.annotations.ValidEnumValue;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.filter.OrderDirection;
import com.abnamro.recipe.domain.filter.recipe.RecipeSortFilters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipeSortFilterRequest {

    @ApiModelProperty(notes = "Sort Recipes by fields", example = "ID")
    @ValidEnumValue(required = true, enumClass = Recipe.SortingFields.class)
    private String orderField = Recipe.SortingFields.ID.name();

    @ApiModelProperty(notes = "Sort Recipes by direction", example = "DESC")
    @ValidEnumValue(required = true, enumClass = OrderDirection.class)
    private String orderDirection = OrderDirection.DESC.name();

    public RecipeSortFilters toEntity() {
        return RecipeSortFilters.builder()
                .fields(Recipe.SortingFields.valueOf(orderField))
                .direction(OrderDirection.valueOf(orderDirection))
                .build();
    }
}
