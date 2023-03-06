package com.abenamro.recipe.domain.usecases.ingredient;

import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.exception.ResourceNotFoundException;
import com.abnamro.recipe.domain.filter.PageFilters;
import com.abnamro.recipe.domain.filter.ingredient.IngredientFilters;
import com.abnamro.recipe.domain.filter.ingredient.IngredientSortFilters;
import com.abnamro.recipe.domain.repository.IngredientRepository;
import com.abnamro.recipe.domain.usecases.ingredient.ReadIngredientUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReadIngredientUseCaseTest {

    private static final Ingredient INGREDIENT_01 = Ingredient.builder()
            .id(123L)
            .name("Name")
            .description("Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Ingredient INGREDIENT_02 = Ingredient.builder()
            .id(124L)
            .name("Fake Name")
            .description("Fake Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    private static final IngredientFilters INGREDIENT_FILTERS = IngredientFilters.builder().build();

    private static final IngredientSortFilters INGREDIENT_SORT_FILTERS = IngredientSortFilters.builder().build();

    private static final PageFilters PAGE_FILTERS = PageFilters.builder().build();

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private ReadIngredientUseCase readIngredientUseCase;

    @Test
    public void readIngredient_success() {

        when(ingredientRepository.findById(123L)).thenReturn(Optional.of(INGREDIENT_01));

        Ingredient ingredient = readIngredientUseCase.execute(123L);

        assertEquals(INGREDIENT_01, ingredient);
        verify(ingredientRepository).findById(123L);
    }

    @Test
    public void ingredient_notExist() {

        when(ingredientRepository.findById(125L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> readIngredientUseCase.execute(125L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(ingredientRepository).findById(125L);

    }

    @Test
    public void readIngredient_success_page() {

        Pageable pageable = PageRequest.of(
                PAGE_FILTERS.getPage() - 1,
                PAGE_FILTERS.getSize(),
                Sort.Direction.fromString(INGREDIENT_SORT_FILTERS.getDirection().toString()),
                INGREDIENT_SORT_FILTERS.getFields().toString().toLowerCase(Locale.ROOT)
        );

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Ingredient> ingredientList = List.of(INGREDIENT_01, INGREDIENT_02);
        Page<Ingredient> ingredientPage = new PageImpl<>(ingredientList, pageRequest, ingredientList.size());

        when(ingredientRepository.findAll(pageable, INGREDIENT_FILTERS.getName(), INGREDIENT_FILTERS.getDescription())).thenReturn(ingredientPage);

        Page<Ingredient> ingredient = readIngredientUseCase.execute(INGREDIENT_FILTERS, INGREDIENT_SORT_FILTERS, PAGE_FILTERS);

        assertEquals(ingredientPage, ingredient);
        assertEquals(ingredientPage.getSize(), 10);
        assertEquals(ingredientPage.getTotalPages(), 1);
        assertEquals(ingredientPage.getContent().size(), 2);
        verify(ingredientRepository).findAll(pageable, INGREDIENT_FILTERS.getName(), INGREDIENT_FILTERS.getDescription());
    }

    @Test
    public void readIngredient_success_page_withFilter() {

        IngredientFilters ingredientFilters = INGREDIENT_FILTERS.toBuilder()
                .name("Name")
                .build();

        Pageable pageable = PageRequest.of(
                PAGE_FILTERS.getPage() - 1,
                PAGE_FILTERS.getSize(),
                Sort.Direction.fromString(INGREDIENT_SORT_FILTERS.getDirection().toString()),
                INGREDIENT_SORT_FILTERS.getFields().toString().toLowerCase(Locale.ROOT)
        );

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Ingredient> ingredientList = List.of(INGREDIENT_01);
        Page<Ingredient> ingredientPage = new PageImpl<>(ingredientList, pageRequest, ingredientList.size());

        when(ingredientRepository.findAll(pageable, ingredientFilters.getName(), ingredientFilters.getDescription())).thenReturn(ingredientPage);

        Page<Ingredient> ingredient = readIngredientUseCase.execute(ingredientFilters, INGREDIENT_SORT_FILTERS, PAGE_FILTERS);

        assertEquals(ingredientPage, ingredient);
        assertEquals(ingredientPage.getSize(), 10);
        assertEquals(ingredientPage.getTotalPages(), 1);
        assertEquals(ingredientPage.getContent().size(), 1);
        verify(ingredientRepository).findAll(pageable, ingredientFilters.getName(), ingredientFilters.getDescription());
    }
}
