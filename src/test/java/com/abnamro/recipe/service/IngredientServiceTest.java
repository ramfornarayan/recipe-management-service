package com.abnamro.recipe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abnamro.recipe.config.MessageProvider;
import com.abnamro.recipe.entity.Ingredient;
import com.abnamro.recipe.exception.NotFoundException;
import com.abnamro.recipe.repository.IngredientRepository;
import com.abnamro.recipe.utils.builder.IngredientTestDataBuilder;
import com.abnamro.request.CreateIngredientRequest;

@ExtendWith(MockitoExtension.class)
class IngredientServiceTest {

	@Mock
	private IngredientRepository ingredientRepository;

	@Mock
	private MessageProvider messageProvider;

	@InjectMocks
	private IngredientService ingredientService;

	@Test
	void test_createIngredient_successfully() {
		CreateIngredientRequest request = IngredientTestDataBuilder.createIngredientRequest();

		Ingredient response = IngredientTestDataBuilder.createIngredient();
		response.setId(5);

		when(ingredientRepository.save(any(Ingredient.class))).thenReturn(response);

		Integer id = ingredientService.create(request);

		assertThat(id).isEqualTo(response.getId());
		verify(ingredientRepository).save(any(Ingredient.class));
	}

	@Test
	void test_deleteIngredient_successfully() {
		when(ingredientRepository.existsById(anyInt())).thenReturn(true);

		doNothing().when(ingredientRepository).deleteById(anyInt());

		ingredientService.delete(5);

		verify(ingredientRepository).existsById(5);
		verify(ingredientRepository).deleteById(5);
	}

	@Test
	void test_deleteIngredient_notFound() {
		when(ingredientRepository.existsById(anyInt())).thenReturn(false);

		assertThrows(NotFoundException.class, () -> ingredientService.delete(1));

		verify(ingredientRepository).existsById(1);
		verify(ingredientRepository, never()).deleteById(anyInt());
	}
}