package com.abnamro.recipe.controller;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.abnamro.recipe.entity.Ingredient;
import com.abnamro.recipe.response.CreateEntityResponse;
import com.abnamro.recipe.response.IngredientResponse;
import com.abnamro.recipe.service.IngredientService;
import com.abnamro.recipe.utils.builder.IngredientTestDataBuilder;
import com.abnamro.request.CreateIngredientRequest;


@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    private IngredientService ingredientService;

    @InjectMocks
    private IngredientController ingredientController;

    @Test
    void test_createIngredient_successfully() {
        when(ingredientService.create(any(CreateIngredientRequest.class)))
                .thenReturn(1);

        CreateIngredientRequest request =
                IngredientTestDataBuilder.createIngredientRequest();

        ResponseEntity<CreateEntityResponse> responseEntity =
                ingredientController.createIngredient(request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(1);
    }

    @Test
    void test_getIngredient_successfully() {
        Ingredient ingredient = IngredientTestDataBuilder.createIngredient();
        ingredient.setId(5);

        when(ingredientService.findById(5)).thenReturn(ingredient);

        ResponseEntity<IngredientResponse> responseEntity =
                ingredientController.getIngredient(5);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(5);
    }

    @Test
    void test_listIngredients_successfully() {
        List<Ingredient> storedList =
                IngredientTestDataBuilder.createIngredientList(true);

        when(ingredientService.list(0, 10)).thenReturn(storedList);
        
        ResponseEntity<List<IngredientResponse>> responseEntity =
                ingredientController.getIngredientList(0, 10);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(storedList.size());
        assertThat(responseEntity.getBody().get(0).getId())
                .isEqualTo(storedList.get(0).getId());
    }

    @Test
    void test_deleteIngredient_successfully() {
        doNothing().when(ingredientService).delete(5);

        ingredientController.deleteIngredient(5);
    }
}