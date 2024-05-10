package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeRegister
import com.cabral.core.common.domain.repository.RecipeRepository
import com.cabral.remote.local.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class RecipeRepositoryImpl(private val remoteDataSource: RemoteDataSource) : RecipeRepository {
    override fun getAllRecipes(): Flow<List<Recipe>> {
        return remoteDataSource.getAllRecipe()
    }

    override fun addRecipe(recipe: Recipe): Flow<String?> {
        return remoteDataSource.addRecipe(recipe)
    }
}