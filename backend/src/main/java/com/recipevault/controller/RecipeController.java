package com.recipevault.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipevault.dto.RecipeRequestDTO;
import com.recipevault.dto.RecipeResponseDTO;
import com.recipevault.model.Recipe;
import com.recipevault.service.RecipeService;
import com.recipevault.util.JwtUtil;

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
    private final JwtUtil jwtUtil;

    public RecipeController(RecipeService recipeService, JwtUtil jwtUtil) {
        this.recipeService = recipeService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping
    public ResponseEntity<List<RecipeResponseDTO>> getAllRecipes() {
        return ResponseEntity.ok(recipeService.getAllRecipes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeResponseDTO> getRecipeById(@PathVariable Long id) {
        Optional<RecipeResponseDTO> recipe = recipeService.getRecipeById(id);
        return recipe.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/my")
    public ResponseEntity<List<RecipeResponseDTO>> getMyRecipes(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String username = jwtUtil.extractUsername(token);
        List<RecipeResponseDTO> myRecipes = recipeService.getRecipesByUser(username);
        return ResponseEntity.ok(myRecipes);
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Recipe> createRecipe(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("title") String title,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("instructions") String instructions,
            @RequestParam("ingredients") String ingredientsJson,
            @RequestParam(value = "image", required = false) MultipartFile image) throws IOException {

        String token = authHeader.substring(7);
        
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ingredients = objectMapper.readValue(ingredientsJson, List.class); 

        RecipeRequestDTO recipeDTO = new RecipeRequestDTO();
        recipeDTO.setTitle(title);
        recipeDTO.setDifficulty(difficulty);
        recipeDTO.setInstructions(instructions);
        recipeDTO.setIngredients(ingredients);
        recipeDTO.setImage(image);

        Recipe savedRecipe = recipeService.createRecipe(token, recipeDTO);
        return ResponseEntity.ok(savedRecipe);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<Recipe> updateRecipe(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("difficulty") String difficulty,
            @RequestParam("instructions") String instructions,
            @RequestParam("ingredients") String ingredientsJson,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "removeExistingImage", required = false, defaultValue = "false") boolean removeExistingImage ) throws IOException {
        
        String token = authHeader.substring(7);

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> ingredients = objectMapper.readValue(ingredientsJson, List.class);

        RecipeRequestDTO recipeDTO = new RecipeRequestDTO();
        recipeDTO.setTitle(title);
        recipeDTO.setDifficulty(difficulty);
        recipeDTO.setInstructions(instructions);
        recipeDTO.setIngredients(ingredients);
        recipeDTO.setImage(image);

        Recipe updatedRecipe = recipeService.updateRecipe(token, id, recipeDTO, removeExistingImage);
        return updatedRecipe != null ? ResponseEntity.ok(updatedRecipe) : ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRecipe(@RequestHeader("Authorization") String authHeader, @PathVariable Long id) {
        String token = authHeader.substring(7);
        boolean deleted = recipeService.deleteRecipe(token, id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.status(403).body("Unauthorized to delete this recipe");
    }
}
