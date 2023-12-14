package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.repository.IngredientRepository
import com.cabral.remote.local.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class IngredientRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : IngredientRepository {
    override fun getAllIngredients(): Flow<List<Ingredient>> {
        return remoteDataSource.getAllIngredients()
    }

    override fun addIngredient(listIngredient: List<Ingredient>): Flow<Unit> {
        return remoteDataSource.addIngredient(listIngredient)
    }

    override fun deleteIngredient(ingredient: Ingredient): Flow<Unit> {
        return remoteDataSource.deleteIngredient(ingredient)
    }

    override fun changeIngredient(ingredient: Ingredient): Flow<Unit> {
        return remoteDataSource.changeIngredient(ingredient)
    }

}