package com.recipevault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipevault.dto.RecipeRequestDTO;
import com.recipevault.model.Recipe;
import com.recipevault.service.RecipeService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Recipe> createRecipe(
            @RequestParam("title") String title,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("instructions") String instructions,
            @RequestParam("creatorName") String creatorName,
            @RequestParam("ingredients") String ingredientsJson,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ingredients = objectMapper.readValue(ingredientsJson, List.class); 

        RecipeRequestDTO recipeDTO = new RecipeRequestDTO();
        recipeDTO.setTitle(title);
        recipeDTO.setDifficulty(difficulty);
        recipeDTO.setInstructions(instructions);
        recipeDTO.setCreatorName(creatorName);
        recipeDTO.setIngredients(ingredients);
        recipeDTO.setImage(image);

        Recipe savedRecipe = recipeService.createRecipe(recipeDTO);
        return ResponseEntity.ok(savedRecipe);
    }

    @GetMapping
    public ResponseEntity<List<Recipe>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable Long id) {
        Optional<Recipe> recipe = recipeService.getRecipeById(id);
        return recipe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Recipe> updateRecipe(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("instructions") String instructions,
            @RequestParam("creatorName") String creatorName,
            @RequestParam("ingredients") String ingredientsJson,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ingredients = objectMapper.readValue(ingredientsJson, List.class);

        RecipeRequestDTO recipeDTO = new RecipeRequestDTO();
        recipeDTO.setTitle(title);
        recipeDTO.setDifficulty(difficulty);
        recipeDTO.setInstructions(instructions);
        recipeDTO.setCreatorName(creatorName);
        recipeDTO.setIngredients(ingredients);
        recipeDTO.setImage(image);

        Recipe updatedRecipe = recipeService.updateRecipe(id, recipeDTO);
        return updatedRecipe != null ? ResponseEntity.ok(updatedRecipe) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return ResponseEntity.noContent().build();
    }
}
