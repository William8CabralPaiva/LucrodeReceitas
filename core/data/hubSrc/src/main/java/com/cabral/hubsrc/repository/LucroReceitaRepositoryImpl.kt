package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.LucroReceitaRepository
import com.cabral.remote.local.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class LucroReceitaRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : LucroReceitaRepository {
    override fun addUser(user: User): Flow<Unit> {
        return remoteDataSource.addUser(user)
    }

    override suspend fun addUser2(user: User) {
        return remoteDataSource.addUser2(user)
    }

    override fun login(user: User): Flow<User> {
        return remoteDataSource.login(user)
    }

    override fun autoLogin(key: String): Flow<User> {
        return remoteDataSource.autoLogin(key)
    }

    override fun googleLogin(email: String, name: String): Flow<User> {
        return remoteDataSource.googleLogin(email, name)
    }

    override fun getAllRecipe(email: String): Flow<List<Recipe>> {
        return getAllRecipe(email)
    }

    override fun getAllIngredients(email: String): Flow<List<Ingredient>> {
        return getAllIngredients(email)
    }
}