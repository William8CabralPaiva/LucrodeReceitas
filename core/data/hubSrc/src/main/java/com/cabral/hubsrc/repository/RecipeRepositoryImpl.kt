package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.core.common.domain.repository.RecipeRepository
import com.cabral.remote.local.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class RecipeRepositoryImpl(private val remoteDataSource: RemoteDataSource) : RecipeRepository {
    override fun getAllRecipes(): Flow<List<RecipeProfitPrice>> {
        return remoteDataSource.getAllRecipes()
    }

    override fun getRecipeByKeyDocument(keyDocument: String): Flow<Recipe> {
        return remoteDataSource.getRecipeByKeyDocument(keyDocument)
    }

    override fun addRecipe(recipe: Recipe): Flow<String?> {
        return remoteDataSource.addRecipe(recipe)
    }

    override fun deleteRecipe(keyDocument: String): Flow<Unit> {
        return remoteDataSource.deleteRecipe(keyDocument)
    }
}