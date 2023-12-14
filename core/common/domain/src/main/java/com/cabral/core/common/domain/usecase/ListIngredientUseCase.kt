package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow

class ListIngredientUseCase(private val repository: IngredientRepository) {
    operator fun invoke(): Flow<List<Ingredient>> {
        return repository.getAllIngredients()
    }
}