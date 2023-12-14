package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow

class AddIngredientUseCase(private val repository: IngredientRepository) {

    operator fun invoke(list: List<Ingredient>): Flow<Unit> {
        return repository.addIngredient(list)
    }
}