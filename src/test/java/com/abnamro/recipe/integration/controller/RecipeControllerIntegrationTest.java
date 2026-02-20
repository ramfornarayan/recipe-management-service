package com.abnamro.recipe.integration.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.test.web.servlet.MvcResult;

import com.abnamro.recipe.entity.Ingredient;
import com.abnamro.recipe.entity.Recipe;
import com.abnamro.recipe.repository.IngredientRepository;
import com.abnamro.recipe.repository.RecipeRepository;
import com.abnamro.recipe.response.RecipeResponse;
import com.abnamro.recipe.response.RecipeSearchResponse;
import com.abnamro.recipe.utils.builder.IngredientTestDataBuilder;
import com.abnamro.recipe.utils.builder.RecipeTestDataBuilder;
import com.abnamro.request.CreateRecipeRequest;
import com.abnamro.request.UpdateRecipeRequest;

class RecipeControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setup() {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
    }

    // ---------------- CREATE ----------------

    @Test
    void test_createRecipe_successfully() throws Exception {
        CreateRecipeRequest request =
                new CreateRecipeRequest("pasta", "VEGETARIAN", 5, null, "someInstruction");

        MvcResult result = performPost("/api/v1/recipe", request)
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readByJsonPath(result, "$.id");

        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        assertTrue(optionalRecipe.isPresent());
        assertEquals("pasta", optionalRecipe.get().getName());
    }

    // ---------------- GET BY ID ----------------

    @Test
    void test_getRecipe_successfully() throws Exception {
        Recipe recipe = RecipeTestDataBuilder.createRecipe();
        Recipe savedRecipe = recipeRepository.save(recipe);

        performGet("/api/v1/recipe/" + savedRecipe.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(savedRecipe.getId()))
                .andExpect(jsonPath("$.name").value(savedRecipe.getName()))
                .andExpect(jsonPath("$.instructions").value(savedRecipe.getInstructions()))
                .andExpect(jsonPath("$.numberOfServings").value(savedRecipe.getNumberOfServings()));
    }

    @Test
    void test_getRecipe_notFound() throws Exception {
        performGet("/api/v1/recipe/999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    // ---------------- LIST ----------------

    @Test
    void test_listRecipe_successfully() throws Exception {
        Recipe recipe1 = new Recipe();
        recipe1.setName("name1");
        recipe1.setInstructions("Ins1");
        recipe1.setType("OTHER");

        Recipe recipe2 = new Recipe();
        recipe2.setName("name2");
        recipe2.setInstructions("Ins2");
        recipe2.setType("OTHER");

        recipeRepository.saveAll(List.of(recipe1, recipe2));

        MvcResult result = performGet("/api/v1/recipe/page/0/size/10")
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeResponse> recipes =
                getListFromMvcResult(result, RecipeResponse.class);

        assertEquals(2, recipes.size());
        assertEquals("name1", recipes.get(0).getName());
        assertEquals("name2", recipes.get(1).getName());
    }

    // ---------------- UPDATE ----------------

    @Test
    void test_updateRecipe_successfully() throws Exception {
        Recipe recipe = RecipeTestDataBuilder.createRecipe();
        Recipe savedRecipe = recipeRepository.save(recipe);

        UpdateRecipeRequest updateRequest =
                new UpdateRecipeRequest(
                        savedRecipe.getId(),
                        "meat-lasagna",
                        "NON_VEGETARIAN",
                        4,
                        null, "add meat"
                );

        performPatch("/api/v1/recipe", updateRequest)
                .andExpect(status().isOk());

        Recipe updated = recipeRepository.findById(savedRecipe.getId()).orElseThrow();
        assertEquals("meat-lasagna", updated.getName());
        assertEquals("add meat", updated.getInstructions());
    }

    @Test
    void test_updateRecipe_idIsNull() throws Exception {
        UpdateRecipeRequest request =
                new UpdateRecipeRequest(null, "sarmale", "OTHER", 2, null, "cook well");

        performPatch("/api/v1/recipe", request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void test_updateRecipe_notFound() throws Exception {
        UpdateRecipeRequest request =
                new UpdateRecipeRequest(999, "ghost", "VEGETARIAN", 1, null, "none");

        performPatch("/api/v1/recipe", request)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    // ---------------- DELETE ----------------

    @Test
    void test_deleteRecipe_successfully() throws Exception {
        Recipe recipe = RecipeTestDataBuilder.createRecipe();
        Recipe savedRecipe = recipeRepository.save(recipe);

        performDelete("/api/v1/recipe",
                Pair.of("id", String.valueOf(savedRecipe.getId())))
                .andExpect(status().isOk());

        assertTrue(recipeRepository.findById(savedRecipe.getId()).isEmpty());
    }

    @Test
    void test_deleteRecipe_notFound() throws Exception {
        performDelete("/api/v1/recipe",
                Pair.of("id", "999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    // ---------------- SEARCH (GET with query params) ----------------

    @Test
    void test_searchRecipe_successfully() throws Exception {
        Ingredient ingredient =
                IngredientTestDataBuilder.createIngredientWithNameParam("Pepper");
        ingredientRepository.save(ingredient);

        CreateRecipeRequest request =
                new CreateRecipeRequest(
                        "pasta",
                        "VEGETARIAN",
                        5,
                        List.of(ingredient.getId()),
                        "boil and mix"
                );

        performPost("/api/v1/recipe", request)
                .andExpect(status().isCreated());

        MvcResult result = performGet(
                "/api/v1/recipe/search?page=0&size=10&instruction=boil")
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeSearchResponse> responses =
                getListFromMvcResult(result, RecipeSearchResponse.class);

        assertEquals(1, responses.size());
        //assertEquals("pasta", responses.get(0).getName());
    }

    @Test
    void test_searchRecipe_noResult() throws Exception {
        MvcResult result = performGet(
                "/api/v1/recipe/search?page=0&size=10&instruction=xyz")
                .andExpect(status().isOk())
                .andReturn();

        List<RecipeSearchResponse> responses =
                getListFromMvcResult(result, RecipeSearchResponse.class);

        assertTrue(responses.isEmpty());
    }
}