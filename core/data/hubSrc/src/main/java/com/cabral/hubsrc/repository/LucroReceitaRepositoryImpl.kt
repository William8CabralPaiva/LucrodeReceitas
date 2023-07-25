package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.LucroReceitaRepository
import com.cabral.remote.local.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class LucroReceitaRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : LucroReceitaRepository {
    override fun addUser(user: User): Flow<Unit> {
        return remoteDataSource.addUser(user)
    }

    override suspend fun addUser2(user: User) {
        return remoteDataSource.addUser2(user)
    }

    override fun login(email: String): Flow<User> {
        return login(email)
    }

    override fun getAllRecipe(email: String): Flow<List<Recipe>> {
        return getAllRecipe(email)
    }

    override fun getAllIngredients(email: String): Flow<List<Ingredient>> {
        return getAllIngredients(email)
    }
}