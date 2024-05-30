package com.cabral.core.common.domain.repository

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getAllRecipes(): Flow<List<RecipeProfitPrice>>
    fun addRecipe(recipe: Recipe): Flow<String?>
    fun deleteRecipe(keyDocument: String): Flow<Unit>
}