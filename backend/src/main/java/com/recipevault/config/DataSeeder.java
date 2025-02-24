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
            recipe1.setTitle("Carbonara");
            recipe1.setDifficulty(Difficulty.MEDIUM);
            recipe1.setInstructions("Cut the guanciale into rectangular pieces and fry them in the pan until golden brown. Boil the pasta according until al dente. Mix the guanciale, hot pasta with freshly beaten egg yolks and grated Parmigiano Reggiano. Finally, serve the fresh carbonara with freshly grounded black pepper.");
            recipe1.setCreatorName("Gino D'Acampo");
            recipe1.setImageUrl("/uploads/carbonara.jpg");

            List<Ingredient> ingredients1 = List.of(
                new Ingredient(null, "Spagetti Noodles", recipe1),
                new Ingredient(null, "Guanciale", recipe1),
                new Ingredient(null, "Egg", recipe1),
                new Ingredient(null, "Parmigiano Reggiano", recipe1)
            );
            recipe1.setIngredients(ingredients1);

            // Create second recipe with ingredients
            Recipe recipe2 = new Recipe();
            recipe2.setTitle("Creamy Scramble Eggs");
            recipe2.setDifficulty(Difficulty.HARD);
            recipe2.setInstructions("Crack 6 cold eggs into a deep saucepan. Add the butter and put the pan on high heat. Stir continuously with a rubber spatula, making sure to scrape the bottom of the pan. After 30 seconds, take the pan off the heat and keep stirring. After about 10 seconds, put back on the heat. Repeat for 3 minutes. Finally, plate and garnish with chopped chives.");
            recipe2.setCreatorName("Gordon Ramsay");
            recipe2.setImageUrl("/uploads/scrambled-eggs.jpg");

            List<Ingredient> ingredients2 = List.of(
                new Ingredient(null, "Eggs", recipe2),
                new Ingredient(null, "Butter", recipe2),
                new Ingredient(null, "Chives", recipe2)
            );
            recipe2.setIngredients(ingredients2);

            // Create third recipe with ingredients
            Recipe recipe3 = new Recipe();
            recipe3.setTitle("Egg Fried Rice");
            recipe3.setDifficulty(Difficulty.MEDIUM);
            recipe3.setInstructions("Separate the egg whites from the yolk. Mix the yolk with the rice, ensure an even golden coat on each grain of rice. Bring the wok into high heat and stir fry the egg whites, then the rice. Season well and serve with some fresh spring onions.");
            recipe3.setCreatorName("Uncle Roger");
            recipe3.setImageUrl("/uploads/fried-rice.jpg");

            List<Ingredient> ingredients3 = List.of(
                new Ingredient(null, "Overnight Rice", recipe3),
                new Ingredient(null, "Kampung Eggs", recipe3),
                new Ingredient(null, "Spring Onion", recipe3)
            );
            recipe3.setIngredients(ingredients3);


            // Create fourth recipe with ingredients
            Recipe recipe4 = new Recipe();
            recipe4.setTitle("Pepperoni Pizza");
            recipe4.setDifficulty(Difficulty.MEDIUM);
            recipe4.setInstructions("Mix the flour and water into a dough and let it rest. After 30 minutes, knead the dough into a circular shape and spread the tomato sauce evenly. Top the pizza with some Mozarella Cheese and Pepperoni. Finally, drizzle some olive oil on the pizza and bake the pizza in oven at 230 Degrees Celcius for 15 minutes.");
            recipe4.setCreatorName("Joshua Weissman");
            recipe4.setImageUrl("/uploads/pizza.jpg");

            List<Ingredient> ingredients4 = List.of(
                new Ingredient(null, "Flour", recipe4),
                new Ingredient(null, "Tomato", recipe4),
                new Ingredient(null, "Cheese", recipe4),
                new Ingredient(null, "Pepperoni", recipe4)
            );
            recipe4.setIngredients(ingredients4);


            // Create fifth recipe with ingredients
            Recipe recipe5 = new Recipe();
            recipe5.setTitle("Fluffy Pancakes");
            recipe5.setDifficulty(Difficulty.EASY);
            recipe5.setInstructions("Mix batter and cook on skillet. Serve the pancakes with some maple syrup and salted butter.");
            recipe5.setCreatorName("Jamie Oliver");
            recipe5.setImageUrl("/uploads/pancakes.jpg");

            List<Ingredient> ingredients5 = List.of(
                new Ingredient(null, "Flour", recipe5),
                new Ingredient(null, "Eggs", recipe5),
                new Ingredient(null, "Milk", recipe5)
            );
            recipe5.setIngredients(ingredients5);


            // Save recipes along with ingredients
            recipeRepository.saveAll(List.of(recipe1, recipe2, recipe3, recipe4, recipe5));
        }
    }
}