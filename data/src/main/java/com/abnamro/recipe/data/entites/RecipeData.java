package com.abnamro.recipe.data.entites;

import com.abnamro.recipe.domain.entities.ERecipeType;
import com.abnamro.recipe.domain.entities.Recipe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@DynamicUpdate
@Table(name = "recipes")
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class RecipeData extends DataBaseEntity {

    @Column(nullable = false)
    private String type;

    @Column(name = "number_of_servings", nullable = false)
    private int numberOfServings;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "recipe_ingredient",
            joinColumns = @JoinColumn(name = "recipe_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    )
    private Set<IngredientData> ingredients;

    public Recipe toEntity() {
        return Recipe.builder()
                .id(this.getId())
                .name(this.getName())
                .description(this.getDescription())
                .type(this.getType() != null ? ERecipeType.valueOf(this.getType()) : null)
                .numberOfServings(this.numberOfServings)
                .ingredients(this.ingredients != null
                        ? ingredients.stream().map(IngredientData::toEntity).collect(Collectors.toSet())
                        : null)
                .createdBy(this.getCreatedBy())
                .createdAt(this.getCreatedAt())
                .updatedBy(this.getUpdatedBy())
                .updatedAt(this.getUpdatedAt())
                .build();
    }
}
