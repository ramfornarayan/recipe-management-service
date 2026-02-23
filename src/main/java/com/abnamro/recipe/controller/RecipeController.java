package com.abnamro.recipe.controller;

import com.abnamro.recipe.entity.Recipe;
import com.abnamro.recipe.response.CreateEntityResponse;
import com.abnamro.recipe.response.RecipeResponse;
import com.abnamro.recipe.response.RecipeSearchResponse;
import com.abnamro.recipe.search.request.RecipeSearchReq;
import com.abnamro.recipe.service.RecipeService;
import com.abnamro.request.CreateRecipeRequest;
import com.abnamro.request.UpdateRecipeRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "Recipe Management", description = "APIs for managing and searching recipes")
@RestController
@RequestMapping(value = "api/v1/recipe")
public class RecipeController {
	private final Logger logger = LoggerFactory.getLogger(RecipeController.class);

	private final RecipeService recipeService;

	@Autowired
	public RecipeController(RecipeService recipeService) {
		this.recipeService = recipeService;
	}

	@Operation(summary = "Create a recipe")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Recipe created"),
			@ApiResponse(responseCode = "400", description = "Bad input") })
	@PostMapping
	public ResponseEntity<CreateEntityResponse> createRecipe(
			@Valid @RequestBody @Parameter(description = "Properties of the recipe", required = true) CreateRecipeRequest request) {
		logger.info("Creating the recipe with properties");
		Integer id = recipeService.createRecipe(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(new CreateEntityResponse(id));
	}

	@Operation(summary = "Update the recipe")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Recipe updated"),
			@ApiResponse(responseCode = "400", description = "Bad input") })
	@PatchMapping
	public ResponseEntity<Void> updateRecipe(
			@Valid @RequestBody @Parameter(description = "Properties of the recipe", required = true) UpdateRecipeRequest updateRecipeRequest) {
		logger.info("Updating the recipe by given properties");
		recipeService.updateRecipe(updateRecipeRequest);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "Delete the recipe")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "404", description = "Recipe not found by the given ID") })
	@DeleteMapping
	public ResponseEntity<Void> deleteRecipe(
			@RequestParam @NotNull(message = "{id.notNull}") @Parameter(description = "Recipe ID", required = true) Integer id) {
		logger.info("Deleting the recipe by its id. Id: {}", id);
		recipeService.deleteRecipe(id);
		return ResponseEntity.ok().build();
	}

	@Operation(summary = "List all recipes")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful request") })
	@GetMapping("/page/{page}/size/{size}")
	public ResponseEntity<List<RecipeResponse>> getRecipeList(@PathVariable int page, @PathVariable int size) {
		logger.info("Getting the recipes");
		List<Recipe> list = recipeService.getRecipeList(page, size);
		List<RecipeResponse> response = list.stream().map(RecipeResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "List one recipe by its ID")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful request"),
			@ApiResponse(responseCode = "404", description = "Recipe not found by the given ID") })
	@GetMapping("/{id}")
	public ResponseEntity<RecipeResponse> getRecipe(
			@PathVariable @Parameter(description = "Recipe ID", required = true) Integer id) {
		logger.info("Getting the recipe by its id. Id: {}", id);
		Recipe recipe = recipeService.getRecipeById(id);
		return ResponseEntity.ok(new RecipeResponse(recipe));
	}


	@Operation(summary = "Search recipes by given parameters")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Successful request"),
			@ApiResponse(responseCode = "404", description = "Different error messages related to criteria and recipe") })
	@GetMapping("/search")
	public ResponseEntity<List<RecipeSearchResponse>> searchRecipe(
			@RequestParam(name = "page", defaultValue = "0") int page,
			@RequestParam(name = "size", defaultValue = "10") int size,
			@RequestParam(required = false) Boolean vegetarian, @RequestParam(required = false) Integer servings,
			@RequestParam(required = false) String includeIngredient,
			@RequestParam(required = false) String excludeIngredient,
			@RequestParam(required = false) String instruction) {

		Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
		RecipeSearchReq r = new RecipeSearchReq();
		r.vegetarian = vegetarian;
		r.servings = servings;
		r.includeIngredient = includeIngredient;
		r.excludeIngredient = excludeIngredient;
		r.instruction = instruction;

		List<RecipeSearchResponse> result = recipeService.SearchByCriteria(r, pageable);

		return ResponseEntity.ok(result);
	}
	
}
