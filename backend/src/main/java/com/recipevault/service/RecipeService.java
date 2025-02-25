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
import java.util.ArrayList;
import java.util.Iterator;
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

    public Recipe updateRecipe(Long id, RecipeRequestDTO recipeDTO) throws IOException {
        Optional<Recipe> optionalRecipe = recipeRepository.findById(id);
        if (optionalRecipe.isEmpty()) {
            return null; // Recipe not found
        }
    
        Recipe recipe = optionalRecipe.get();
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setDifficulty(Difficulty.valueOf(recipeDTO.getDifficulty().toUpperCase()));
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setCreatorName(recipeDTO.getCreatorName());
    
        // Clear existing ingredients using Iterator to avoid orphan deletion errors
        Iterator<Ingredient> iterator = recipe.getIngredients().iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }

        // Add new ingredients
        List<Ingredient> newIngredients = new ArrayList<>();
        for (String ingredientName : recipeDTO.getIngredients()) {
            newIngredients.add(new Ingredient(null, ingredientName, recipe));
        }
        recipe.getIngredients().addAll(newIngredients); 
    
        // Check if new image is uploaded
        MultipartFile image = recipeDTO.getImage();
        if (image != null && !image.isEmpty()) {
            String fileName = UUID.randomUUID() + "-" + image.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.write(filePath, image.getBytes());
    
            // Remove old image if exists
            if (recipe.getImageUrl() != null) {
                Path oldFilePath = Paths.get(uploadDir, recipe.getImageUrl().replace("/uploads/", ""));
                Files.deleteIfExists(oldFilePath);
            }
    
            // Update image URL
            recipe.setImageUrl("/uploads/" + fileName);
        }
    
        return recipeRepository.save(recipe);
    }
    
    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}
