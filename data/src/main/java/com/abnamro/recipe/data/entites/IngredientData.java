package com.abnamro.recipe.data.entites;

import com.abnamro.recipe.domain.entities.Ingredient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@DynamicUpdate
@Table(name = "ingredients")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class IngredientData extends DataBaseEntity {

    public IngredientData(Ingredient ingredient) {
        super(ingredient);
    }

    public Ingredient toEntity() {
        return Ingredient.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .createdBy(this.getCreatedBy())
                .createdAt(this.getCreatedAt())
                .updatedBy(this.getUpdatedBy())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
