package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.core.common.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow

class GetListRecipeUseCase(private val repository: RecipeRepository) {
    operator fun invoke(): Flow<List<RecipeProfitPrice>> {
        return repository.getAllRecipes()
    }
}