package com.abnamro.recipe.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abnamro.recipe.config.MessageProvider;
import com.abnamro.recipe.entity.Ingredient;
import com.abnamro.recipe.entity.Recipe;
import com.abnamro.recipe.exception.NotFoundException;
import com.abnamro.recipe.repository.RecipeRepository;
import com.abnamro.recipe.response.RecipeSearchResponse;
import com.abnamro.recipe.search.request.RecipeSearchReq;
import com.abnamro.recipe.search.request.RecipeSpecification;
import com.abnamro.request.CreateRecipeRequest;
import com.abnamro.request.UpdateRecipeRequest;

@Service
@Transactional
public class RecipeService {
	private final RecipeRepository recipeRepository;
	private final IngredientService ingredientService;
	private final MessageProvider messageProvider;

	@Autowired
	public RecipeService(RecipeRepository recipeRepository, IngredientService ingredientService,
			MessageProvider messageProvider) {
		this.recipeRepository = recipeRepository;
		this.ingredientService = ingredientService;
		this.messageProvider = messageProvider;
	}

	public Integer createRecipe(CreateRecipeRequest createRecipeRequest) {
		Set<Ingredient> ingredients = Optional.ofNullable(createRecipeRequest.getIngredientIds())
				.map(ingredientService::getIngredientsByIds).orElse(null);

		Recipe recipe = new Recipe();
		recipe.setName(createRecipeRequest.getName());
		recipe.setInstructions(createRecipeRequest.getInstructions());
		recipe.setType(createRecipeRequest.getType());
		recipe.setNumberOfServings(createRecipeRequest.getNumberOfServings());
		recipe.setRecipeIngredients(ingredients);

		Recipe createdRecipe = recipeRepository.save(recipe);

		return createdRecipe.getId();
	}

	public List<Recipe> getRecipeList(int page, int size) {
		Pageable pageRequest = PageRequest.of(page, size);
		return recipeRepository.findAll(pageRequest).getContent();
	}

	public Recipe getRecipeById(int id) {
		return recipeRepository.findById(id)
				.orElseThrow(() -> new NotFoundException(messageProvider.getMessage("recipe.notFound")));
	}

	public void updateRecipe(UpdateRecipeRequest updateRecipeRequest) {
		Recipe recipe = recipeRepository.findById(updateRecipeRequest.getId())
				.orElseThrow(() -> new NotFoundException(messageProvider.getMessage("recipe.notFound")));

		Set<Ingredient> ingredients = Optional.ofNullable(updateRecipeRequest.getIngredientIds())
				.map(ingredientService::getIngredientsByIds).orElse(null);

		recipe.setName(updateRecipeRequest.getName());
		recipe.setType(updateRecipeRequest.getType());
		recipe.setNumberOfServings(updateRecipeRequest.getNumberOfServings());
		recipe.setInstructions(updateRecipeRequest.getInstructions());

		if (Optional.ofNullable(ingredients).isPresent())
			recipe.setRecipeIngredients(ingredients);

		recipeRepository.save(recipe);
	}

	public void deleteRecipe(int id) {
		if (!recipeRepository.existsById(id)) {
			throw new NotFoundException(messageProvider.getMessage("recipe.notFound"));
		}

		recipeRepository.deleteById(id);
	}

	public List<RecipeSearchResponse> SearchByCriteria(RecipeSearchReq request, Pageable pageable) {
		return recipeRepository.findAll(RecipeSpecification.build(request), pageable).getContent() // ✅ extract List
																									// from Page
				.stream().map(recipe -> {
					RecipeSearchResponse r = new RecipeSearchResponse();
					r.id = recipe.getId();
					r.name = recipe.getName();
					r.type = recipe.getType();
					r.numberOfServings = recipe.getNumberOfServings();
					r.instructions = recipe.getInstructions();
					r.ingredients = recipe.getRecipeIngredients().stream().map(Ingredient::getName).toList();
					return r;
				}).toList();
	}

}
