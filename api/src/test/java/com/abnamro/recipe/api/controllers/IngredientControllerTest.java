package com.abnamro.recipe.api.controllers;

import com.abnamro.recipe.api.payloads.response.ApiResponsePayload;
import com.abnamro.recipe.api.payloads.response.ingredient.IngredientResponse;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.filter.PageFilters;
import com.abnamro.recipe.domain.filter.ingredient.IngredientFilters;
import com.abnamro.recipe.domain.filter.ingredient.IngredientSortFilters;
import com.abnamro.recipe.domain.usecases.ingredient.CreateIngredientUseCase;
import com.abnamro.recipe.domain.usecases.ingredient.DeleteIngredientUseCase;
import com.abnamro.recipe.domain.usecases.ingredient.ReadIngredientUseCase;
import com.abnamro.recipe.domain.usecases.ingredient.UpdateIngredientUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(IngredientRestController.class)
public class IngredientControllerTest extends BaseControllerTest {

    private static final String BASE_ENDPOINT = "/api/v1/ingredient";

    private static final Ingredient VALID_INGREDIENT = Ingredient.builder()
            .id(123L)
            .name("Fake Name")
            .description("Fake Description")
            .createdBy("fakeemail@fakeserver.com")
            .updatedBy("fakeemail@fakeserver.com")
            .build();

    private static final Ingredient VALID_INGREDIENT_01 = Ingredient.builder()
            .id(123L)
            .name("Fake Name Second")
            .description("Fake Description Second")
            .createdBy("fakeemail01@fakeserver.com")
            .updatedBy("fakeemail01@fakeserver.com")
            .build();

    private static final Long INGREDIENT_ID = 123L;

    @MockBean
    private ReadIngredientUseCase readIngredientUseCase;
    @MockBean
    private CreateIngredientUseCase createIngredientUseCase;
    @MockBean
    private UpdateIngredientUseCase updateIngredientUseCase;
    @MockBean
    private DeleteIngredientUseCase deleteIngredientUseCase;

    @Test
    public void getIngredient_success() throws Exception {

        when(readIngredientUseCase.execute(INGREDIENT_ID)).thenReturn(VALID_INGREDIENT);

        ApiResponsePayload<IngredientResponse> response = request(
                get(BASE_ENDPOINT + "/" + INGREDIENT_ID),
                ApiResponsePayload.class
        );

        ObjectMapper mapper = new ObjectMapper();
        IngredientResponse ingredientResponse = mapper.convertValue(response.getData(), IngredientResponse.class);

        assertThat(response).isNotNull();
        assertThat(ingredientResponse.getName()).isEqualTo("Fake Name");
        assertThat(ingredientResponse.getUpdatedBy()).isEqualTo("fakeemail@fakeserver.com");
        verify(readIngredientUseCase).execute(INGREDIENT_ID);
    }

    @Test
    public void fetchAllIngredient_byFilters_success() throws Exception {

        IngredientFilters ingredientFilters = IngredientFilters.builder().build();
        IngredientSortFilters ingredientSortFilters = IngredientSortFilters.builder().build();
        PageFilters pageFilters = PageFilters.builder().build();

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<Ingredient> ingredientList = List.of(VALID_INGREDIENT, VALID_INGREDIENT_01);
        Page<Ingredient> ingredientPage = new PageImpl<>(ingredientList, pageRequest, ingredientList.size());

        when(readIngredientUseCase.execute(ingredientFilters, ingredientSortFilters, pageFilters)).thenReturn(ingredientPage);

        MockHttpServletResponse response = request(get(BASE_ENDPOINT + "/fetch/all"), HttpStatus.OK).andReturn().getResponse();

        assertThat(response).isNotNull();
        verify(readIngredientUseCase).execute(ingredientFilters, ingredientSortFilters, pageFilters);
    }

    @Test
    public void createIngredient_success() throws Exception {
        String payload = "ingredient/valid_ingredient_values.json";

        Ingredient ingredient = Ingredient.builder()
                .name("Fake Name")
                .description("Fake Description")
                .createdBy("fakeemail@fakeserver.com")
                .build();

        when(createIngredientUseCase.execute(ingredient)).thenReturn(VALID_INGREDIENT);

        ApiResponsePayload<IngredientResponse> response = request(
                post(BASE_ENDPOINT),
                payload,
                HttpStatus.CREATED,
                ApiResponsePayload.class
        );

        ObjectMapper mapper = new ObjectMapper();
        IngredientResponse ingredientResponse = mapper.convertValue(response.getData(), IngredientResponse.class);

        assertThat(response).isNotNull();
        assertThat(ingredientResponse.getName()).isEqualTo("Fake Name");
        assertThat(ingredientResponse.getUpdatedBy()).isEqualTo("fakeemail@fakeserver.com");
        verify(createIngredientUseCase).execute(ingredient);
    }

    @Test
    public void createIngredient_InvalidRequest() throws Exception {
        String payload = "ingredient/invalid_ingredient_values.json";

        HttpMessageNotReadableException response = request(
                post(BASE_ENDPOINT),
                payload,
                HttpStatus.BAD_REQUEST,
                HttpMessageNotReadableException.class
        );

        assertThat(response.getMessage()).isEqualTo("Ingredient Name can't be blank");
        verifyNoInteractions(createIngredientUseCase);
    }

    @Test
    public void updateIngredient_success() throws Exception {
        String payload = "ingredient/valid_update_ingredient_values.json";

        Ingredient ingredient = Ingredient.builder()
                .id(123L)
                .name("Fake Name Second")
                .description("Fake Description Second")
                .updatedBy("fakeemailUpdate01@fakeserver.com")
                .build();

        when(updateIngredientUseCase.execute(ingredient)).thenReturn(VALID_INGREDIENT_01);

        ApiResponsePayload<IngredientResponse> response = request(
                patch(BASE_ENDPOINT),
                payload,
                HttpStatus.OK,
                ApiResponsePayload.class
        );

        ObjectMapper mapper = new ObjectMapper();
        IngredientResponse ingredientResponse = mapper.convertValue(response.getData(), IngredientResponse.class);

        assertThat(response).isNotNull();
        assertThat(ingredientResponse.getName()).isEqualTo("Fake Name Second");
        assertThat(ingredientResponse.getUpdatedBy()).isEqualTo("fakeemail01@fakeserver.com");
        verify(updateIngredientUseCase).execute(ingredient);
    }

    @Test
    public void deleteIngredient_success() throws Exception {

        request(
                delete(BASE_ENDPOINT + "/" + INGREDIENT_ID),
                HttpStatus.OK
        );

        verify(deleteIngredientUseCase).execute(INGREDIENT_ID);
    }

}