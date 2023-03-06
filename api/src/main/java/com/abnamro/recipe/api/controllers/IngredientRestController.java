package com.abnamro.recipe.api.controllers;

import com.abnamro.recipe.api.payloads.request.PageFiltersRequest;
import com.abnamro.recipe.api.payloads.request.ingredient.IngredientCreateRequest;
import com.abnamro.recipe.api.payloads.request.ingredient.IngredientFilterRequest;
import com.abnamro.recipe.api.payloads.request.ingredient.IngredientSortFilterRequest;
import com.abnamro.recipe.api.payloads.request.ingredient.IngredientUpdateRequest;
import com.abnamro.recipe.api.payloads.response.ApiResponsePayload;
import com.abnamro.recipe.api.payloads.response.ingredient.IngredientResponse;
import com.abnamro.recipe.domain.entities.Ingredient;
import com.abnamro.recipe.domain.usecases.ingredient.CreateIngredientUseCase;
import com.abnamro.recipe.domain.usecases.ingredient.DeleteIngredientUseCase;
import com.abnamro.recipe.domain.usecases.ingredient.ReadIngredientUseCase;
import com.abnamro.recipe.domain.usecases.ingredient.UpdateIngredientUseCase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "IngredientRestController", tags = "list ingredients, create ingredients, update ingredients, delete ingredients")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/ingredient")
public class IngredientRestController {

    private final ReadIngredientUseCase readIngredientUseCase;
    private final CreateIngredientUseCase createIngredientUseCase;
    private final UpdateIngredientUseCase updateIngredientUseCase;
    private final DeleteIngredientUseCase deleteIngredientUseCase;

    @ApiOperation(value = "Get ingredient by existing ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Ingredient not found by the given ID")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponsePayload<IngredientResponse> getExistIngredient(
            @ApiParam(value = "Ingredient ID", required = true)
            @NotNull(message = "Ingredient ID is required")
            @PathVariable(name = "id") long id
    ) {
        log.info("Get ingredient by existing ID: {}", id);

        Ingredient existIngredient = this.readIngredientUseCase.execute(id);

        return ApiResponsePayload.<IngredientResponse>builder()
                .data(new IngredientResponse(existIngredient))
                .build();
    }

    @ApiOperation(value = "Fetch all Ingredients by filters")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful request")})
    @RequestMapping(method = RequestMethod.GET, path = "/fetch/all")
    public ApiResponsePayload<List<IngredientResponse>> fetchAllPageable(
            @Valid IngredientFilterRequest ingredientFilterRequest,
            @Valid IngredientSortFilterRequest ingredientSortFilterRequest,
            @Valid PageFiltersRequest pageFiltersRequest
    ) {
        log.info("Fetching all ingredients filtered by {}", ingredientFilterRequest);

        Page<Ingredient> ingredients = this.readIngredientUseCase.execute(
                ingredientFilterRequest.toEntity(),
                ingredientSortFilterRequest.toEntity(),
                pageFiltersRequest.toEntity()
        );

        return ApiResponsePayload.<List<IngredientResponse>>builder()
                .pageable(ingredients.getPageable())
                .data(ingredients.getContent().stream().map(IngredientResponse::new).collect(Collectors.toList()))
                .build();
    }

    @ApiOperation(value = "Create a new Ingredient")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Ingredient created Successfully"),
            @ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponsePayload<IngredientResponse> createNewIngredient(
            @ApiParam(value = "Ingredient Properties", required = true)
            @Valid @RequestBody IngredientCreateRequest ingredientCreateRequest
    ) {
        log.info("Create a new Ingredient by Properties {}", ingredientCreateRequest);

        Ingredient newIngredient = this.createIngredientUseCase.execute(ingredientCreateRequest.toEntity());

        return ApiResponsePayload.<IngredientResponse>builder()
                .data(new IngredientResponse(newIngredient))
                .build();
    }

    @ApiOperation(value = "Update exist Ingredient By ID")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Ingredient Update"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @RequestMapping(method = RequestMethod.PATCH)
    public ApiResponsePayload<IngredientResponse> updateExistIngredient(
            @ApiParam(value = "Properties of the Ingredient", required = true)
            @Valid @RequestBody IngredientUpdateRequest ingredientUpdateRequest
    ) {
        log.info("Updating the Ingredient by Properties {}", ingredientUpdateRequest);

        Ingredient updatedIngredient = this.updateIngredientUseCase.execute(ingredientUpdateRequest.toEntity());

        return ApiResponsePayload.<IngredientResponse>builder()
                .data(new IngredientResponse(updatedIngredient))
                .build();
    }

    @ApiOperation(value = "Delete Exist ingredient by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 404, message = "Ingredient not found by the given ID")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteExistIngredient(
            @ApiParam(value = "ingredient ID", required = true)
            @NotNull(message = "Ingredient ID is required")
            @PathVariable(name = "id") long id
    ) {
        log.info("Deleting the ingredient by its id. Id: {}", id);

        this.deleteIngredientUseCase.execute(id);
    }
}
