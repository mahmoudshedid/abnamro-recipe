package com.abnamro.recipe.api.controllers;

import com.abnamro.recipe.api.payloads.request.PageFiltersRequest;
import com.abnamro.recipe.api.payloads.request.recipe.RecipeCreateRequest;
import com.abnamro.recipe.api.payloads.request.recipe.RecipeFilterRequest;
import com.abnamro.recipe.api.payloads.request.recipe.RecipeSortFilterRequest;
import com.abnamro.recipe.api.payloads.request.recipe.RecipeUpdateRequest;
import com.abnamro.recipe.api.payloads.response.ApiResponsePayload;
import com.abnamro.recipe.api.payloads.response.recipe.RecipeResponse;
import com.abnamro.recipe.domain.entities.Recipe;
import com.abnamro.recipe.domain.usecases.recipe.CreateRecipeUseCase;
import com.abnamro.recipe.domain.usecases.recipe.DeleteRecipeUseCase;
import com.abnamro.recipe.domain.usecases.recipe.ReadRecipeUseCase;
import com.abnamro.recipe.domain.usecases.recipe.UpdateRecipeUseCase;
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

@Api(value = "RecipeRestController", tags = "list recipes, create recipes, update recipes, delete recipes")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/recipe")
public class RecipeRestController {

    private final ReadRecipeUseCase readRecipeUseCase;
    private final CreateRecipeUseCase createRecipeUseCase;
    private final UpdateRecipeUseCase updateRecipeUseCase;
    private final DeleteRecipeUseCase deleteRecipeUseCase;

    @ApiOperation(value = "Get Recipe by existing ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful request"),
            @ApiResponse(code = 404, message = "Recipes not found by the given ID")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ApiResponsePayload<RecipeResponse> getExistRecipes(
            @ApiParam(value = "Recipe ID", required = true)
            @NotNull(message = "Recipe ID is required")
            @PathVariable(name = "id") long id
    ) {
        log.info("Get Recipe by existing ID: {}", id);

        Recipe existRecipe = this.readRecipeUseCase.execute(id);

        return ApiResponsePayload.<RecipeResponse>builder()
                .data(new RecipeResponse(existRecipe))
                .build();
    }

    @ApiOperation(value = "Fetch all Recipes by filters")
    @ApiResponses(value = {@io.swagger.annotations.ApiResponse(code = 200, message = "Successful request")})
    @RequestMapping(method = RequestMethod.GET, path = "/fetch/all")
    public ApiResponsePayload<List<RecipeResponse>> fetchAllPageable(
            @Valid RecipeFilterRequest recipeFilterRequest,
            @Valid RecipeSortFilterRequest recipeSortFilterRequest,
            @Valid PageFiltersRequest pageFiltersRequest
    ) {
        log.info("Fetching all recipes filtered by {}", recipeFilterRequest);

        Page<Recipe> recipes = this.readRecipeUseCase.execute(
                recipeFilterRequest.toEntity(),
                recipeSortFilterRequest.toEntity(),
                pageFiltersRequest.toEntity()
        );

        return ApiResponsePayload.<List<RecipeResponse>>builder()
                .pageable(recipes.getPageable())
                .data(recipes.getContent().stream().map(RecipeResponse::new).collect(Collectors.toList()))
                .build();
    }

    @ApiOperation(value = "Create a new Recipe")
    @ApiResponses(value = {
            @io.swagger.annotations.ApiResponse(code = 201, message = "Recipe created Successfully"),
            @io.swagger.annotations.ApiResponse(code = 400, message = "Bad request")
    })
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponsePayload<RecipeResponse> createNewRecipe(
            @ApiParam(value = "Recipe Properties", required = true)
            @Valid @RequestBody RecipeCreateRequest recipeCreateRequest
    ) {
        log.info("Create a new Recipe with Properties {}", recipeCreateRequest);

        Recipe newRecipe = this.createRecipeUseCase.execute(recipeCreateRequest.toEntity());

        return ApiResponsePayload.<RecipeResponse>builder()
                .data(new RecipeResponse(newRecipe))
                .build();
    }

    @ApiOperation(value = "Update exist Recipe By ID")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Recipe Update"),
            @ApiResponse(code = 400, message = "Bad input")
    })
    @RequestMapping(method = RequestMethod.PATCH)
    public ApiResponsePayload<RecipeResponse> updateExistRecipe(
            @ApiParam(value = "Properties of the Recipe", required = true)
            @Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest
    ) {
        log.info("Updating the Recipe by Properties {}", recipeUpdateRequest);

        Recipe updatedRecipe = this.updateRecipeUseCase.execute(recipeUpdateRequest.toEntity());

        return ApiResponsePayload.<RecipeResponse>builder()
                .data(new RecipeResponse(updatedRecipe))
                .build();
    }

    @ApiOperation(value = "Delete Exist Recipe by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 400, message = "Invalid input"),
            @ApiResponse(code = 404, message = "Recipe not found by the given ID")
    })
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteExistRecipe(
            @ApiParam(value = "Recipe ID", required = true)
            @NotNull(message = "Recipe ID is required")
            @PathVariable(name = "id") long id
    ) {
        log.info("Deleting the Recipe by its id. Id: {}", id);

        this.deleteRecipeUseCase.execute(id);
    }
}
