package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.repository.IngredientRepository
import com.cabral.remote.local.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class IngredientRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : IngredientRepository {
    override fun addIngredient(ingredient: List<Ingredient>): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteIngredient(ingredient: Ingredient): Flow<Unit> {
        TODO("Not yet implemented")
    }

    override fun changeIngredient(ingredient: Ingredient): Flow<Unit> {
        TODO("Not yet implemented")
    }

}