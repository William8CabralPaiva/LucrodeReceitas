package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeRegister
import com.cabral.core.common.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class AddRecipeUseCase(
    private val repository: RecipeRepository
) {
    operator fun invoke(recipe: Recipe): Flow<Unit> {
        return repository.addRecipe(recipe)
    }
}