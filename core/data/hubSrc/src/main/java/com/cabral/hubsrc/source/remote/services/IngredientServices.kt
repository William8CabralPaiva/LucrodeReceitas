package com.cabral.hubsrc.source.remote.services

import com.cabral.core.common.domain.model.Ingredient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface IngredientServices {
    fun getAllIngredients(): Flow<List<Ingredient>> = flow {}

    fun addIngredient(ingredientList: List<Ingredient>): Flow<Unit> = flow {}

    fun deleteIngredient(ingredient: Ingredient): Flow<Unit> = flow {}
}