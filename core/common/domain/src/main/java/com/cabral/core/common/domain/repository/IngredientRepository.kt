package com.cabral.core.common.domain.repository

import com.cabral.core.common.domain.model.Ingredient
import kotlinx.coroutines.flow.Flow

interface IngredientRepository {
    fun getAllIngredients(): Flow<List<Ingredient>>
    fun addIngredient(listIngredient: List<Ingredient>): Flow<Unit>

    fun deleteIngredient(ingredient: Ingredient): Flow<Unit>

    fun changeIngredient(ingredient: Ingredient): Flow<Unit>
}