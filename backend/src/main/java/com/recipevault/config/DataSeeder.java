package com.recipevault.config;

import com.recipevault.model.Difficulty;
import com.recipevault.model.Ingredient;
import com.recipevault.model.Recipe;
import com.recipevault.repository.RecipeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataSeeder {
    private final RecipeRepository recipeRepository;

    public DataSeeder(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @PostConstruct
    public void seedData() {
        if (recipeRepository.count() == 0) {
            // Create first recipe with ingredients
            Recipe recipe1 = new Recipe();
            recipe1.setTitle("Spaghetti Bolognese");
            recipe1.setDifficulty(Difficulty.MEDIUM);
            recipe1.setInstructions("Boil pasta and cook sauce.");
            recipe1.setCreatorName("Chef John");

            List<Ingredient> ingredients1 = List.of(
                new Ingredient(null, "Tomato", recipe1),
                new Ingredient(null, "Garlic", recipe1),
                new Ingredient(null, "Olive Oil", recipe1)
            );
            recipe1.setIngredients(ingredients1);

            // Create second recipe with ingredients
            Recipe recipe2 = new Recipe();
            recipe2.setTitle("Pancakes");
            recipe2.setDifficulty(Difficulty.EASY);
            recipe2.setInstructions("Mix batter and cook on skillet.");
            recipe2.setCreatorName("Baker Sam");

            List<Ingredient> ingredients2 = List.of(
                new Ingredient(null, "Flour", recipe2),
                new Ingredient(null, "Eggs", recipe2),
                new Ingredient(null, "Milk", recipe2)
            );
            recipe2.setIngredients(ingredients2);

            // Save recipes along with ingredients
            recipeRepository.saveAll(List.of(recipe1, recipe2));
        }
    }
}