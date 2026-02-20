package com.abnamro.recipe.response;

import com.abnamro.recipe.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import io.swagger.v3.oas.annotations.media.Schema;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class RecipeResponse {
	@Schema(description = "The id of the returned recipe", example = "1")
	private int id;

	@Schema(description = "The name of the returned recipe", example = "Pasta")
	private String name;

	@Schema(description = "The type of the returned recipe", example = "VEGETARIAN")
	private String type;

	@Schema(description = "Number of servings", example = "1")
	private int numberOfServings;

	@JsonIgnoreProperties("ingredients")
	private Set<IngredientResponse> ingredients;

	@Schema(description = "The instructions of the returned recipe", example = "Chop the onion, add to pasta and boil it")
	private String instructions;

	@CreationTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	private LocalDateTime updatedAt;

	public RecipeResponse() {
	}

	public RecipeResponse(Recipe recipe) {
		this.id = recipe.getId();
		this.name = recipe.getName();
		this.type = recipe.getType();
		this.instructions = recipe.getInstructions();
		this.createdAt = recipe.getCreatedAt();
		this.updatedAt = recipe.getUpdatedAt();
		this.numberOfServings = recipe.getNumberOfServings();

		this.ingredients = recipe.getRecipeIngredients() != null
				? recipe.getRecipeIngredients().stream().map(IngredientResponse::new).collect(Collectors.toSet())
				: null;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumberOfServings() {
		return numberOfServings;
	}

	public void setNumberOfServings(int numberOfServings) {
		this.numberOfServings = numberOfServings;
	}

	public Set<IngredientResponse> getIngredients() {
		return ingredients;
	}

	public void setIngredients(Set<IngredientResponse> ingredients) {
		this.ingredients = ingredients;
	}

	public String getInstructions() {
		return instructions;
	}

	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}
