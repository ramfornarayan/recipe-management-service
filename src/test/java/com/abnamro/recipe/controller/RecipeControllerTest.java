package com.abnamro.recipe.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.abnamro.recipe.entity.Recipe;
import com.abnamro.recipe.response.CreateEntityResponse;
import com.abnamro.recipe.response.RecipeResponse;
import com.abnamro.recipe.service.RecipeService;
import com.abnamro.request.CreateRecipeRequest;
import com.abnamro.request.UpdateRecipeRequest;

@ExtendWith(MockitoExtension.class)
class RecipeControllerTest {

    @Mock
    private RecipeService recipeService;

    @InjectMocks
    private RecipeController recipeController;

    @Test
    void test_createRecipe_successfully() {
        CreateRecipeRequest request = new CreateRecipeRequest("pasta", "OTHER", 4, null, "instructions");

        when(recipeService.createRecipe(any(CreateRecipeRequest.class))).thenReturn(1);

        ResponseEntity<CreateEntityResponse> responseEntity = recipeController.createRecipe(request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(1);
    }

    @Test
    void test_getRecipe_successfully() {
        Recipe recipe = new Recipe();
        recipe.setId(5);
        recipe.setName("name");

        when(recipeService.getRecipeById(anyInt())).thenReturn(recipe);

        ResponseEntity<RecipeResponse> responseEntity = recipeController.getRecipe(5);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody().getId()).isEqualTo(recipe.getId());
        assertThat(responseEntity.getBody().getName()).isEqualTo(recipe.getName());
    }

    @Test
    void test_listRecipes_successfully() {
        Recipe recipe1 = new Recipe();
        recipe1.setId(5);
        recipe1.setName("name1");

        Recipe recipe2 = new Recipe();
        recipe2.setId(6);
        recipe2.setName("name2");

        List<Recipe> storedRecipeList = new ArrayList<>();
        storedRecipeList.add(recipe1);
        storedRecipeList.add(recipe2);

        when(recipeService.getRecipeList(anyInt(), anyInt())).thenReturn(storedRecipeList);

        ResponseEntity<List<RecipeResponse>> responseEntity = recipeController.getRecipeList(0, 10);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).hasSize(2);
        assertThat(responseEntity.getBody().get(0).getId()).isEqualTo(5);
        assertThat(responseEntity.getBody().get(1).getId()).isEqualTo(6);
    }

    @Test
    void test_updateRecipe_successfully() {
        doNothing().when(recipeService).updateRecipe(any(UpdateRecipeRequest.class));

        UpdateRecipeRequest request = new UpdateRecipeRequest(1, "pasta", "OTHER", 4, null, "instructions");

        ResponseEntity<Void> responseEntity = recipeController.updateRecipe(request);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    void test_deleteRecipe_successfully() {
        // Assuming deleteRecipe returns boolean in controller
        //doReturn(true).when(recipeService).deleteRecipe(anyInt());
        doNothing().when(recipeService).deleteRecipe(anyInt());
        ResponseEntity<Void> responseEntity = recipeController.deleteRecipe(5);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isNull();
    }

  /*  @Test
    void test_deleteRecipe_notFound() {
        // Delete failed scenario
       // doReturn(false).when(recipeService).deleteRecipe(10);

        ResponseEntity<Void> responseEntity = recipeController.deleteRecipe(10);

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }*/
}