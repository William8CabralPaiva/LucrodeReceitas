package com.cabral.core.common.domain.repository

import com.cabral.core.common.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getAllRecipes(): Flow<List<Recipe>>
}