package com.recipevault.dto;

import com.recipevault.model.Ingredient;
import com.recipevault.model.UserInfo;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeResponseDTO {
    private Long id;
    private String title;
    private String difficulty;
    private String instructions;
    private String imageUrl;
    private LocalDateTime createdDate;
    private List<Ingredient> ingredients;
    private Long userId;
    private String fullName; 

    public static RecipeResponseDTO fromRecipe(com.recipevault.model.Recipe recipe) {
        return new RecipeResponseDTO(
                recipe.getId(),
                recipe.getTitle(),
                recipe.getDifficulty().name(),
                recipe.getInstructions(),
                recipe.getImageUrl(),
                recipe.getCreatedDate(),
                recipe.getIngredients(),
                recipe.getUser().getId(),
                recipe.getUser().getFullName()
        );
    }
}