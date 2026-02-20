package com.abnamro.recipe.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.abnamro.recipe.entity.Recipe;

@DataJpaTest
public class RecipeRepositoryTest {
	@Autowired
	private RecipeRepository recipeRepository;

	@Test
	public void test_whenTryToSaveIngredientSuccess() {
		Recipe entity = new Recipe();
		entity.setType("Other");
		entity.setInstructions("some instructions");
		entity.setName("pasta");
		Recipe savedRecipe = recipeRepository.save(entity);
		assertNotNull(savedRecipe);

		assertEquals("Other", savedRecipe.getType());
		assertNotNull(savedRecipe.getId());
	}

	@Test
	public void test_whenTryGetTokenListSuccess() {
		Recipe entity1 = new Recipe();
		entity1.setType("Other");
		entity1.setName("lasagna");

		Recipe entity2 = new Recipe();
		entity2.setType("Other");
		entity2.setName("pizza");

		Recipe firstSavedEntity = recipeRepository.save(entity1);
		Recipe secondSavedEntity = recipeRepository.save(entity2);
		assertNotNull(firstSavedEntity);
		assertNotNull(secondSavedEntity);

		assertFalse(recipeRepository.findAll().isEmpty());
		assertEquals(2, recipeRepository.findAll().size());
	}
}