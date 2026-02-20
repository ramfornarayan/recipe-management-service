package com.abnamro.recipe.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.abnamro.recipe.entity.Ingredient;

@DataJpaTest
public class IngredientRepositoryTest {
	@Autowired
	private IngredientRepository ingredientRepository;

	@Test
	public void test_whenTryToSaveIngredientSuccess() {
		Ingredient entity = new Ingredient();
		entity.setIngredientName("Tomato");
		Ingredient savedIngredient = ingredientRepository.save(entity);
		assertNotNull(savedIngredient);

		assertEquals("Tomato", savedIngredient.getName());
		assertNotNull(savedIngredient.getId());
	}

	@Test
	public void test_whenTryGetTokenListSuccess() {
		Ingredient entity1 = new Ingredient();
		entity1.setIngredientName("Tomato");

		Ingredient entity2 = new Ingredient();
		entity2.setIngredientName("Potato");

		Ingredient firstSavedEntity = ingredientRepository.save(entity1);
		Ingredient secondSavedEntity = ingredientRepository.save(entity2);
		assertNotNull(firstSavedEntity);
		assertNotNull(secondSavedEntity);

		assertFalse(ingredientRepository.findAll().isEmpty());
		assertEquals(2, ingredientRepository.findAll().size());
	}

	 @Test
	    void test_whenTryAddSameIngredientTwice_thenFails() {

	        Ingredient entity1 = new Ingredient();
	        entity1.setIngredientName("Tomato");

	        Ingredient entity2 = new Ingredient();
	        entity2.setIngredientName("Tomato");

	        ingredientRepository.saveAndFlush(entity1);

	        assertThrows(DataIntegrityViolationException.class, () -> {
	            ingredientRepository.saveAndFlush(entity2);
	        });
	    }
	
	
}