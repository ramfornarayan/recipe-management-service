package com.abnamro.recipe.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.abnamro.recipe.entity.Ingredient;
import com.abnamro.recipe.response.CreateEntityResponse;
import com.abnamro.recipe.response.IngredientResponse;
import com.abnamro.recipe.service.IngredientService;
import com.abnamro.request.CreateIngredientRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "IngredientController", description = "APIs for managing Create, update, delete, list ingredients")
@RestController
@RequestMapping("api/v1/ingredient")
public class IngredientController {

	private final Logger logger = LoggerFactory.getLogger(IngredientController.class);

	private final IngredientService ingredientService;

	@Autowired
	public IngredientController(IngredientService ingredientService) {
		this.ingredientService = ingredientService;
	}

	@Operation(summary = "Create an ingredient")
	@ApiResponses({ @ApiResponse(responseCode = "201", description = "Ingredient created"),
			@ApiResponse(responseCode = "400", description = "Bad input") })
	@PostMapping
	public ResponseEntity<CreateEntityResponse> createIngredient(
			@Parameter(description = "Properties of the Ingredient", required = true) @Valid @RequestBody CreateIngredientRequest request) {
		logger.info("Creating the ingredient with properties");
		Integer id = ingredientService.create(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(new CreateEntityResponse(id));
	}

	@Operation(summary = "List all ingredients")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful request") })
	@GetMapping("/page/{page}/size/{size}")
	public ResponseEntity<List<IngredientResponse>> getIngredientList(@PathVariable(name = "page") int page,
			@PathVariable(name = "size") int size) {
		logger.info("Getting the ingredients");
		List<Ingredient> list = ingredientService.list(page, size);
		List<IngredientResponse> response = list.stream().map(IngredientResponse::new).collect(Collectors.toList());
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "List one ingredient by its ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful request"),
			@ApiResponse(responseCode = "404", description = "Ingredient not found by the given ID") })
	@GetMapping("/{id}")
	public ResponseEntity<IngredientResponse> getIngredient(
			@Parameter(description = "Ingredient ID", required = true) @PathVariable(name = "id") Integer id) {
		logger.info("Getting the ingredient by its id. Id: {}", id);
		Ingredient ingredient = ingredientService.findById(id);
		if (ingredient == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(new IngredientResponse(ingredient));
	}

	@Operation(summary = "Delete the ingredient")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Successful operation"),
			@ApiResponse(responseCode = "400", description = "Invalid input"),
			@ApiResponse(responseCode = "404", description = "Ingredient not found by the given ID") })
	@DeleteMapping()
	public void deleteIngredient(
			@Parameter(description = "ingredient ID", required = true) @NotNull(message = "{id.notNull}") @RequestParam(name = "id") Integer id) {
		logger.info("Deleting the ingredient by its id. Id: {}", id);
		ingredientService.delete(id);
	}
}