package com.recipevault.service;

import com.recipevault.model.Ingredient;
import com.recipevault.model.Recipe;
import com.recipevault.repository.RecipeRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        // Dependency Injection
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe createRecipe(Recipe recipe) {
        for (Ingredient ingredient : recipe.getIngredients()) {
            ingredient.setRecipe(recipe);
        }
        return recipeRepository.save(recipe);
    }

    // public Recipe updateRecipe(Long id, Recipe updatedRecipe) {
    //     if (recipeRepository.existsById(id)) {
    //         updatedRecipe.setId(id);
    //         return recipeRepository.save(updatedRecipe);
    //     }

    //     return null;
    // }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}
