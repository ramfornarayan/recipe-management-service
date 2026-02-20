package com.abnamro.recipe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.abnamro.recipe.config.MessageProvider;
import com.abnamro.recipe.entity.Recipe;
import com.abnamro.recipe.exception.NotFoundException;
import com.abnamro.recipe.repository.RecipeRepository;
import com.abnamro.request.CreateRecipeRequest;
import com.abnamro.request.UpdateRecipeRequest;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private IngredientService ingredientService;

    @Mock
    private MessageProvider messageProvider;

    @InjectMocks
    private RecipeService recipeService;

    @Test
    void test_createRecipe_successfully() {
        CreateRecipeRequest request =
                new CreateRecipeRequest("pasta", "OTHER", 4, null, "instructions");

        Recipe response = new Recipe();
        response.setId(5);
        response.setName("pasta");

        when(recipeRepository.save(any(Recipe.class)))
                .thenReturn(response);

        Integer id = recipeService.createRecipe(request);

        assertThat(id).isEqualTo(response.getId());
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void test_updateRecipe_successfully() {
        Recipe existingRecipe = new Recipe();
        existingRecipe.setId(1);
        existingRecipe.setName("old");

        UpdateRecipeRequest request =
                new UpdateRecipeRequest(1, "pasta", "OTHER", 4, null, "instructions");

        when(recipeRepository.findById(1))
                .thenReturn(Optional.of(existingRecipe));

        when(recipeRepository.save(any(Recipe.class)))
                .thenReturn(existingRecipe);

        recipeService.updateRecipe(request);

        verify(recipeRepository).findById(1);
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void test_updateRecipe_notFound() {
        UpdateRecipeRequest request =
                new UpdateRecipeRequest(1, "pasta", "OTHER", 4, null, "instructions");

        when(recipeRepository.findById(anyInt()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                recipeService.updateRecipe(request)
        );

        verify(recipeRepository).findById(1);
        verify(recipeRepository, never()).save(any());
    }

    @Test
    void test_deleteRecipe_successfully() {
        when(recipeRepository.existsById(1))
                .thenReturn(true);

        doNothing().when(recipeRepository).deleteById(1);

        recipeService.deleteRecipe(1);

        verify(recipeRepository).existsById(1);
        verify(recipeRepository).deleteById(1);
    }

    @Test
    void test_deleteRecipe_notFound() {
        when(recipeRepository.existsById(1))
                .thenReturn(false);

        assertThrows(NotFoundException.class, () ->
                recipeService.deleteRecipe(1)
        );

        verify(recipeRepository).existsById(1);
        verify(recipeRepository, never()).deleteById(anyInt());
    }

/*    @Test
    void test_findBySearchCriteria_notFound() {
        RecipeSearchRequest request = mock(RecipeSearchRequest.class);

        assertThrows(NotFoundException.class, () ->
                recipeService.findBySearchCriteria(request, 0, 10)
        );
    }*/
}