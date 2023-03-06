package com.abnamro.recipe.api.payloads.request.ingredient;

import com.abnamro.recipe.api.constant.Validation;
import com.abnamro.recipe.domain.entities.Ingredient;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class IngredientUpdateRequest {

    @ApiModelProperty(notes = "Ingredient ID", example = "1")
    @NotNull(message = "Ingredient ID can't be blank")
    @Positive(message = "Ingredient ID should be positive")
    private long id;

    @ApiModelProperty(notes = "Ingredient Name", example = "Meat")
    @Pattern(regexp = Validation.NAME_PATTERN, message = "Enter a valid Ingredient Name")
    @NotBlank(message = "Ingredient Name can't be blank")
    @Size(max = Validation.MAX_LENGTH_NAME, message = "Ingredient Name Shouldn't be more than 100")
    private String name;

    @ApiModelProperty(notes = "Ingredient Description", example = "Adding Meat to the recipe with other ingredients")
    @Pattern(regexp = Validation.FREE_TEXT_PATTERN, message = "Enter a valid Ingredient Description")
    @Size(max = Validation.MAX_LENGTH_FREE_TEXT, message = "Ingredient Description Shouldn't be more than 255")
    private String description;

    @ApiModelProperty(notes = "Ingredient Updated By", example = "example@example.com")
    @Pattern(regexp = Validation.EMAIL_PATTERN, message = "Enter a valid email")
    @NotBlank(message = "Ingredient Updated By Can't be blank")
    @Size(max = Validation.MAX_LENGTH_NAME, message = "Ingredient Updated By 'email' Shouldn't be more than 100")
    private String updatedBy;

    public Ingredient toEntity() {
        return Ingredient.builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .updatedBy(this.updatedBy)
                .build();
    }
}
