package com.abnamro.recipe.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@AllArgsConstructor
public class Ingredient extends BaseEntity {

    public enum SortingFields {
        ID, NAME, CREATED_BY, CREATED_AT, UPDATED_BY, UPDATED_AT
    }
}
