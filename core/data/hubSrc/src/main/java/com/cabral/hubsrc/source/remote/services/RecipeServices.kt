package com.cabral.hubsrc.source.remote.services

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface RecipeServices {
    fun getAllRecipes(): Flow<List<RecipeProfitPrice>> = flow {}

    fun getRecipeByKeyDocument(keyDocument: String): Flow<Recipe> = flow {}

    fun addRecipe(recipe: Recipe): Flow<String?> = flow {}

    fun deleteRecipe(keyDocument: String): Flow<Unit> = flow {}
}