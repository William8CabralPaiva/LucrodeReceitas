package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow

class DeleteIngredientUseCase(private val repository: IngredientRepository) {

    operator fun invoke(ingredient: Ingredient): Flow<Unit> {
        return repository.deleteIngredient(ingredient)
    }
}