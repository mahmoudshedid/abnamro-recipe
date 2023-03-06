package com.abnamro.recipe.data.repository;

import com.abnamro.recipe.data.entites.IngredientData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepositoryDataJpa extends JpaRepository<IngredientData, Long> {

    @Query(
            "SELECT ing FROM IngredientData ing " +
                    "WHERE (LOWER(ing.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name is null OR :name = '') " +
                    "AND (LOWER(ing.description) LIKE LOWER(CONCAT('%', :description, '%')) OR :description is null OR :description = '')"
    )
    Page<IngredientData> findAll(@Param("name") String name, @Param("description") String description, Pageable pageable);
}
