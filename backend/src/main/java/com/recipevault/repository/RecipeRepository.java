package com.recipevault.repository;

import com.recipevault.model.Recipe;
import com.recipevault.model.UserInfo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByUser(UserInfo user);
}