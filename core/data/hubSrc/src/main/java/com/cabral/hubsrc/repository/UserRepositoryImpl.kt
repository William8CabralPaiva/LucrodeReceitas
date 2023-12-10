package com.cabral.hubsrc.repository

import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.UserRepository
import com.cabral.remote.local.RemoteDataSource
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : UserRepository {
    override fun addUser(user: User): Flow<Unit> {
        return remoteDataSource.addUser(user)
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

    override suspend fun forgotPassword(email: String) {
        remoteDataSource.forgotPassword(email)
    }

//    override fun getAllRecipe(email: String): Flow<List<Recipe>> {
//        return getAllRecipe(email)
//    }
//
//    override fun getAllIngredients(email: String): Flow<List<Ingredient>> {
//        return getAllIngredients(email)
//    }
}