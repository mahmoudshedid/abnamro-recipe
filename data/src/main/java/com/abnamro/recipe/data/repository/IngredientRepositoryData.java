package com.abnamro.recipe.data.repository;

import com.abnamro.recipe.data.entites.IngredientData;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Repository
@Transactional
@RequiredArgsConstructor
public class IngredientRepositoryData implements IngredientRepository {
    private final IngredientRepositoryDataJpa ingredientRepositoryDataJpa;

    @Override
    @Transactional(readOnly = true)
    public Optional<Ingredient> findById(long id) {
        return this.ingredientRepositoryDataJpa.findById(id).map(IngredientData::toEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ingredient> findAll(Pageable pageable, String name, String description) {
        return this.ingredientRepositoryDataJpa.findAll(name, description, pageable).map(IngredientData::toEntity);
    }

    @Override
    public Ingredient save(Ingredient ingredient) {
        return this.ingredientRepositoryDataJpa.save(
                IngredientData.builder()
                        .id(ingredient.getId())
                        .name(ingredient.getName())
                        .description(ingredient.getDescription())
                        .createdBy(ingredient.getCreatedBy())
                        .createdAt(ingredient.getCreatedAt())
                        .updatedBy(ingredient.getUpdatedBy())
                        .updatedAt(ingredient.getUpdatedAt())
                        .build()
        ).toEntity();
    }

    @Override
    public void delete(long id) {
        this.ingredientRepositoryDataJpa.deleteById(id);
    }

    @Override
    public void deleteAll() {
        this.ingredientRepositoryDataJpa.deleteAll();
    }
}
