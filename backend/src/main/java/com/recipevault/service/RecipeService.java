package com.recipevault.service;

import com.recipevault.dto.RecipeRequestDTO;
import com.recipevault.model.Difficulty;
import com.recipevault.model.Ingredient;
import com.recipevault.model.Recipe;
import com.recipevault.repository.RecipeRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RecipeService {
    private final RecipeRepository recipeRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    private void createUploadDirectory() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdir(); 
        }
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Optional<Recipe> getRecipeById(Long id) {
        return recipeRepository.findById(id);
    }

    public Recipe createRecipe(RecipeRequestDTO recipeDTO) throws IOException {
        createUploadDirectory();

        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setDifficulty(Difficulty.valueOf(recipeDTO.getDifficulty().toUpperCase()));
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setCreatorName(recipeDTO.getCreatorName());

        // Save Image if uploaded
        MultipartFile image = recipeDTO.getImage();
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, image.getBytes());
            recipe.setImageUrl("/uploads/" + fileName);
        }

        // Save Ingredients
        List<Ingredient> ingredients = recipeDTO.getIngredients().stream()
                .map(ingredientName -> new Ingredient(null, ingredientName, recipe))
                .toList();
        recipe.setIngredients(ingredients);

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
