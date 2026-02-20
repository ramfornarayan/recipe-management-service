package com.abnamro.request;

import com.abnamro.recipe.config.ValidationConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateIngredientRequest {
	@NotBlank(message = "{ingredient.notBlank}")
	@Size(max = ValidationConfig.MAX_LENGTH_NAME, message = "{ingredient.size}")
	@Pattern(regexp = ValidationConfig.PATTERN_NAME, message = "{ingredient.pattern}")
	@Schema(description = "The name of the ingredient", example = "Potato")
	private String name;

	public CreateIngredientRequest() {
	}

	public CreateIngredientRequest(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
