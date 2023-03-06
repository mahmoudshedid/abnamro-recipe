package com.abnamro.recipe.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class Recipe extends BaseEntity {

    private ERecipeType type;
    private int numberOfServings;
    private Set<Ingredient> ingredients;

    public enum SortingFields {
        ID, NAME, TYPE, CREATED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT
    }
}
