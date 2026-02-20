package com.abnamro.request;

import com.abnamro.recipe.config.ValidationConfig;
import com.abnamro.recipe.entity.RecipeType;
import com.abnamro.recipe.validator.EnumValidator;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.util.List;

public class UpdateRecipeRequest extends BasicRequest {
	@NotBlank(message = "{recipeName.notBlank}")
	@Size(max = ValidationConfig.MAX_LENGTH_NAME, message = "{recipeName.size}")
	@Pattern(regexp = ValidationConfig.PATTERN_NAME, message = "{recipeName.pattern}")
	@Schema(description = "The name of the ingredient", example = "Potato")
	private String name;

	@EnumValidator(enumClass = RecipeType.class, message = "{recipeType.invalid}")
	@Schema(description = "The type of the recipe", example = "VEGETARIAN")
	private String type;

	@NotNull(message = "{numberOfServings.notNull}")
	@Positive(message = "{numberOfServings.positive}")
	@Schema(description = "The number of servings", example = "7")
	private int numberOfServings;

	@Schema(description = "The new ids of the ingredients needed for the update", example = "[3,4]")
	private List<Integer> ingredientIds;

	@NotBlank(message = "{instructions.notBlank}")
	@Size(max = ValidationConfig.MAX_LENGTH_DEFAULT, message = "{instructions.size}")
	@Pattern(regexp = ValidationConfig.PATTERN_FREE_TEXT, message = "{instructions.pattern}")
	@Schema(description = "The instructions to update the recipe", example = "Cut,fry,eat")

	private String instructions;

	public UpdateRecipeRequest() {
	}

	public UpdateRecipeRequest(Integer id, String recipeName, String type, int numberOfServings, List<Integer> ingredientIds,
			String instructions) {
		super(id);
		this.name = recipeName;
		this.type = type;
		this.numberOfServings = numberOfServings;
		this.ingredientIds = ingredientIds;
		this.instructions = instructions;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getNumberOfServings() {
		return numberOfServings;
	}

	public List<Integer> getIngredientIds() {
		return ingredientIds;
	}

	public String getInstructions() {
		return instructions;
	}
}
