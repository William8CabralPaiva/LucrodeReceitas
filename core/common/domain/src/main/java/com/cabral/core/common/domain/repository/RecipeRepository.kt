package com.cabral.core.common.domain.repository

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeRegister
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<Recipe>>
    fun addRecipe(recipe: Recipe): Flow<String?>
}