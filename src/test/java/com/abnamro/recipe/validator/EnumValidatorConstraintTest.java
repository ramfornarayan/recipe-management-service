package com.abnamro.recipe.validator;

import com.abnamro.request.CreateIngredientRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class EnumValidatorConstraintTest {

    private static Validator validator;

    @BeforeAll
    static void setupValidatorInstance() {
        validator = Validation.buildDefaultValidatorFactory()
                .getValidator();
    }

    @Test
    void whenNotBlankName_thenNoConstraintViolations() {
        CreateIngredientRequest request =
                new CreateIngredientRequest("pasta");

        Set<ConstraintViolation<CreateIngredientRequest>> violations =
                validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void whenNullName_thenOneConstraintViolation() {
        CreateIngredientRequest request =
                new CreateIngredientRequest(null);

        Set<ConstraintViolation<CreateIngredientRequest>> violations =
                validator.validate(request);

        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertEquals("{ingredient.notBlank}", message);
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenEmptyName_thenOneConstraintViolation() {
        CreateIngredientRequest request =
                new CreateIngredientRequest("");

        Set<ConstraintViolation<CreateIngredientRequest>> violations =
                validator.validate(request);

        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertEquals("{ingredient.notBlank}", message);
        assertThat(violations).hasSize(1);
    }

    @Test
    void whenNameDoesNotFitPattern_thenOneConstraintViolation() {
        CreateIngredientRequest request =
                new CreateIngredientRequest("-.1!@$!#@");

        Set<ConstraintViolation<CreateIngredientRequest>> violations =
                validator.validate(request);

        String message = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));

        assertEquals("{ingredient.pattern}", message);
        assertThat(violations).hasSize(1);
    }
}