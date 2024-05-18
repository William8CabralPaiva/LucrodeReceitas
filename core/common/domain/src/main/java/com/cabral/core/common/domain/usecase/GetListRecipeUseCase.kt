package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetListRecipeUseCase(private val repository: RecipeRepository) {
    operator fun invoke(): Flow<List<Recipe>> {
        return repository.getAllRecipes()
    }
}