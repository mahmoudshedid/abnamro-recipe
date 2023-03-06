package com.abnamro.recipe.api.payloads.request.recipe;

import com.abnamro.recipe.api.constant.Validation;
import com.abnamro.recipe.api.validations.annotations.ValidEnumValue;
import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.filter.recipe.RecipeFilters;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class RecipeFilterRequest {

    @ApiModelProperty(notes = "Search for Recipe by Name", example = "Meat Stack")
    @Pattern(regexp = Validation.NAME_PATTERN, message = "Enter a valid Recipe Name")
    @Size(max = Validation.MAX_LENGTH_NAME, message = "Recipe Name Shouldn't be more than 100")
    private String name;

    @ApiModelProperty(notes = "Search for Recipe by Name", example = "Is a flat cut of beef with parallel faces")
    @Pattern(regexp = Validation.FREE_TEXT_PATTERN, message = "Enter a valid Recipe Description")
    @Size(max = Validation.MAX_LENGTH_FREE_TEXT, message = "Recipe Description Shouldn't be more than 255")
    private String description;

    @ApiModelProperty(notes = "Search for Recipe by Type", example = "MEAT")
    @ValidEnumValue(enumClass = ERecipeType.class)
    private String type;

    @ApiModelProperty(notes = "Search for Recipe by number of servings", example = "3")
    @Min(value = 0L, message = "Recipe number of servings should be positive")
    private int numberOfServings;

    @ApiModelProperty(notes = "Search for Recipe by Created By", example = "example@example.com")
    @Pattern(regexp = Validation.EMAIL_PATTERN, message = "Enter a valid email")
    @Size(max = Validation.MAX_LENGTH_NAME, message = "Recipe Created By 'email' Shouldn't be more than 100")
    private String createdBy;

    @ApiModelProperty(notes = "Search for Recipe by Updated By", example = "example@example.com")
    @Pattern(regexp = Validation.EMAIL_PATTERN, message = "Enter a valid email")
    @Size(max = Validation.MAX_LENGTH_NAME, message = "Recipe Updated By 'email' Shouldn't be more than 100")
    private String updatedBy;

    public RecipeFilters toEntity() {
        return RecipeFilters.builder()
                .name(this.name)
                .description(this.description)
                .type(this.type != null ? ERecipeType.valueOf(this.type) : null)
                .numberOfServings(this.numberOfServings)
                .createdBy(this.createdBy)
                .updatedBy(this.updatedBy)
                .build();
    }
}
