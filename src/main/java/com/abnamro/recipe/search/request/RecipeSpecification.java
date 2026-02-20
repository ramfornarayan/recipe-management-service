
package com.abnamro.recipe.search.request;

import org.springframework.data.jpa.domain.Specification;

import com.abnamro.recipe.entity.Ingredient;
import com.abnamro.recipe.entity.Recipe;

import jakarta.persistence.criteria.*;
import java.util.*;

public class RecipeSpecification {

    public static Specification<Recipe> build(RecipeSearchReq r) {
        return (root, query, cb) -> {
            query.distinct(true);
            List<Predicate> predicates = new ArrayList<>();

            if (r.vegetarian != null) {
                predicates.add(cb.equal(root.get("type"),
                        r.vegetarian ? "VEGETARIAN" : "NON_VEGETARIAN"));
            }

            if (r.servings != null) {
                predicates.add(cb.equal(root.get("numberOfServings"), r.servings));
            }

            if (r.instruction != null) {
                predicates.add(cb.like(cb.lower(root.get("instructions")),
                        "%" + r.instruction.toLowerCase() + "%"));
            }

            if (r.includeIngredient != null) {
                Join<Recipe, Ingredient> join = root.join("recipeIngredients");
                predicates.add(cb.equal(cb.lower(join.get("name")),
                        r.includeIngredient.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
