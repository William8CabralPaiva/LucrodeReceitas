package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeCosts
import com.cabral.core.common.domain.model.toRecipeCosts
import com.cabral.core.common.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class CostRecipeUseCase(private val repository: IngredientRepository) {
    operator fun invoke(recipe: Recipe): Flow<RecipeCosts> = flow {

        val result = repository.getAllIngredients()

        result.catch {
            throw Throwable()
        }

        result.collect { costs ->
            val resultRecipeCosts = recipe.toRecipeCosts(costs)
            emit(resultRecipeCosts)

        }

    }
}