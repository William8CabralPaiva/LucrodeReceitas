package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class DeleteRecipeUseCase(private val repository: RecipeRepository) {
    operator fun invoke(keyDocument: String): Flow<Unit> {
        return repository.deleteRecipe(keyDocument)
    }
}