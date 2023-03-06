package com.abnamro.recipe.api.payloads.request.ingredient;

import com.abnamro.recipe.api.constant.Validation;
import com.abnamro.recipe.domain.filter.ingredient.IngredientFilters;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class IngredientFilterRequest {

    @ApiModelProperty(notes = "Search for Ingredient by Name", example = "Meat")
    @Pattern(regexp = Validation.NAME_PATTERN, message = "Enter a valid Ingredient Name")
    @Size(max = Validation.MAX_LENGTH_NAME, message = "Ingredient Name Shouldn't be more than 100")
    private String name;

    @ApiModelProperty(notes = "Search for Ingredient by Name", example = "Meat")
    @Pattern(regexp = Validation.FREE_TEXT_PATTERN, message = "Enter a valid Ingredient Description")
    @Size(max = Validation.MAX_LENGTH_FREE_TEXT, message = "Ingredient Description Shouldn't be more than 255")
    private String description;

    @ApiModelProperty(notes = "Search for Ingredient by Created By", example = "example@example.com")
    @Pattern(regexp = Validation.EMAIL_PATTERN, message = "Enter a valid email")
    @Size(max = Validation.MAX_LENGTH_NAME, message = "Ingredient Created By 'email' Shouldn't be more than 100")
    private String createdBy;

    @ApiModelProperty(notes = "Search for Ingredient by Updated By", example = "example@example.com")
    @Pattern(regexp = Validation.EMAIL_PATTERN, message = "Enter a valid email")
    @Size(max = Validation.MAX_LENGTH_NAME, message = "Ingredient Updated By 'email' Shouldn't be more than 100")
    private String updatedBy;

    public IngredientFilters toEntity() {
        return IngredientFilters.builder()
                .name(this.name)
                .description(this.description)
                .createdBy(this.createdBy)
                .updatedBy(this.updatedBy)
                .build();
    }
}
