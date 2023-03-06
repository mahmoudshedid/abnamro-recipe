package com.abnamro.recipe.data.repository;

import com.abnamro.recipe.data.entites.RecipeData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepositoryDataJpa extends JpaRepository<RecipeData, Long> {

    @Query(
            "SELECT rp FROM RecipeData rp " +
                    "WHERE (LOWER(rp.name) LIKE LOWER(CONCAT('%', :name, '%')) OR :name is null OR :name = '') " +
                    "AND (LOWER(rp.description) LIKE LOWER(CONCAT('%', :description, '%')) OR :description is null OR :description = '') " +
                    "AND (rp.type = :type OR :type is NULL OR :type = '') " +
                    "AND (rp.numberOfServings = :numberOfServings OR :numberOfServings is NULL OR :numberOfServings = 0)"
    )
    Page<RecipeData> findAll(@Param("name") String name,
                             @Param("description") String description,
                             @Param("type") String type,
                             @Param("numberOfServings") int numberOfServings,
                             Pageable pageable);
}
