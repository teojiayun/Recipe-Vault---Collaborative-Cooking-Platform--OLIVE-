package com.recipevault.service;

import com.recipevault.dto.RecipeRequestDTO;
import com.recipevault.dto.RecipeResponseDTO;
import com.recipevault.model.Difficulty;
import com.recipevault.model.Ingredient;
import com.recipevault.model.Recipe;
import com.recipevault.model.UserInfo;
import com.recipevault.repository.RecipeRepository;
import com.recipevault.repository.UserRepository;
import com.recipevault.util.JwtUtil;

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
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    private void createUploadDirectory() {
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdir(); 
        }
    }

    public List<RecipeResponseDTO> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(RecipeResponseDTO:: fromRecipe)
                .toList();
    }


    public Optional<RecipeResponseDTO> getRecipeById(Long id) {
        return recipeRepository.findById(id).map(RecipeResponseDTO::fromRecipe);
    }


    public List<RecipeResponseDTO> getRecipesByUser(String username) {
        UserInfo user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
        return recipeRepository.findByUser(user).stream()
                .map(RecipeResponseDTO::fromRecipe)
                .toList();
    }


    public Recipe createRecipe(String token, RecipeRequestDTO recipeDTO) throws IOException {
        createUploadDirectory();

        // Get User Information
        String username = jwtUtil.extractUsername(token);
        UserInfo user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        // Create Recipe
        Recipe recipe = new Recipe();
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setDifficulty(Difficulty.valueOf(recipeDTO.getDifficulty().toUpperCase()));
        recipe.setInstructions(recipeDTO.getInstructions());
        recipe.setUser(user);

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



    public Recipe updateRecipe(String token, Long id, RecipeRequestDTO recipeDTO, boolean removeExistingImage) throws IOException {
        String username = jwtUtil.extractUsername(token);
        Recipe recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null || !recipe.getUser().getUsername().equals(username)) {
            return null; // Not the owner
        }
    
        // Recipe recipe = optionalRecipe.get();
        recipe.setTitle(recipeDTO.getTitle());
        recipe.setDifficulty(Difficulty.valueOf(recipeDTO.getDifficulty().toUpperCase()));
        recipe.setInstructions(recipeDTO.getInstructions());
    
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
        
        // Handle Image Removal
        if (removeExistingImage && recipe.getImageUrl() != null) {
            // deleteImageFromStorage(recipe.getImageUrl()); 
            recipe.setImageUrl(null); // Clear image reference in database
        }

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

    public boolean deleteRecipe(String token, Long id) {
        String username = jwtUtil.extractUsername(token);
        Recipe recipe = recipeRepository.findById(id).orElse(null);

        if (recipe == null || !recipe.getUser().getUsername().equals(username)) {
            return false; // Unauthorized
        }

        recipeRepository.delete(recipe);
        return true;
    }
}
