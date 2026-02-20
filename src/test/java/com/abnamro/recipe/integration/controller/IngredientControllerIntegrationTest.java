package com.abnamro.recipe.integration.controller;

import com.abnamro.recipe.entity.Ingredient;
import com.abnamro.recipe.repository.IngredientRepository;
import com.abnamro.recipe.response.IngredientResponse;
import com.abnamro.recipe.utils.builder.IngredientTestDataBuilder;
import com.abnamro.request.CreateIngredientRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class IngredientControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        ingredientRepository.deleteAll();
    }

    // -------------------- CREATE --------------------

    @Test
    void test_createIngredient_successfully() throws Exception {
        CreateIngredientRequest request =
                IngredientTestDataBuilder.createIngredientRequest();

        MvcResult result = performPost("/api/v1/ingredient", request)
                .andExpect(status().isCreated())
                .andReturn();

        Integer id = readByJsonPath(result, "$.id");

        Optional<Ingredient> ingredient = ingredientRepository.findById(id);
        assertTrue(ingredient.isPresent());
        assertEquals(request.getName(), ingredient.get().getName());
    }

    @Test
    void test_createIngredient_responseBody() throws Exception {
        CreateIngredientRequest request =
                IngredientTestDataBuilder.createIngredientRequest();

        performPost("/api/v1/ingredient", request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void test_createIngredient_validationFails() throws Exception {
        CreateIngredientRequest request = new CreateIngredientRequest("");
        performPost("/api/v1/ingredient", request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void test_createIngredient_duplicateIngredient() throws Exception {
        Ingredient ingredient =
                ingredientRepository.save(
                        IngredientTestDataBuilder.createIngredient());

        CreateIngredientRequest request =
                new CreateIngredientRequest(ingredient.getName());

        performPost("/api/v1/ingredient", request)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message").exists());
    }

    // -------------------- GET BY ID --------------------

    @Test
    void test_findIngredientById_successfully() throws Exception {
        Ingredient savedIngredient =
                ingredientRepository.save(
                        IngredientTestDataBuilder.createIngredient());

        MvcResult result = performGet("/api/v1/ingredient/" + savedIngredient.getId())
                .andExpect(status().isOk())
                .andReturn();

        IngredientResponse response =
                getFromMvcResult(result, IngredientResponse.class);

        assertEquals(savedIngredient.getName(), response.getName());
    }

    @Test
    void test_findIngredientById_notFound() throws Exception {
        performGet("/api/v1/ingredient/999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    // -------------------- LIST --------------------

    @Test
    void test_listIngredients_successfully() throws Exception {
        ingredientRepository.saveAll(
                IngredientTestDataBuilder.createIngredientList());

        MvcResult result =
                performGet("/api/v1/ingredient/page/0/size/10")
                        .andExpect(status().isOk())
                        .andReturn();

        List<IngredientResponse> responses =
                getListFromMvcResult(result, IngredientResponse.class);

        assertFalse(responses.isEmpty());
    }

    @Test
    void test_listIngredients_emptyList() throws Exception {
        MvcResult result =
                performGet("/api/v1/ingredient/page/0/size/10")
                        .andExpect(status().isOk())
                        .andReturn();

        List<IngredientResponse> responses =
                getListFromMvcResult(result, IngredientResponse.class);

        assertTrue(responses.isEmpty());
    }

    // -------------------- DELETE --------------------

    @Test
    void test_deleteIngredient_successfully() throws Exception {
        Ingredient savedIngredient =
                ingredientRepository.save(
                        IngredientTestDataBuilder.createIngredient());

        performDelete("/api/v1/ingredient?id=" + savedIngredient.getId())
                .andExpect(status().isOk());

        assertEquals(0, ingredientRepository.count());
    }

    @Test
    void test_deleteIngredient_notFound() throws Exception {
        performDelete("/api/v1/ingredient?id=999")
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void test_deleteIngredient_missingIdParam() throws Exception {
        performDelete("/api/v1/ingredient")
                .andExpect(status().isBadRequest());
    }

    // -------------------- DATE FORMAT --------------------

    @Test
    void test_getIngredient_dateFormat() throws Exception {
        Ingredient ingredient =
                ingredientRepository.save(
                        IngredientTestDataBuilder.createIngredient());

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        performGet("/api/v1/ingredient/" + ingredient.getId())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.createdAt")
                        .value(ingredient.getCreatedAt().format(formatter)))
                .andExpect(jsonPath("$.updatedAt")
                        .value(ingredient.getUpdatedAt().format(formatter)));
    }
}