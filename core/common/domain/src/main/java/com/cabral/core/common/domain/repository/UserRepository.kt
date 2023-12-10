package com.cabral.core.common.domain.repository

import com.cabral.core.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun addUser(user: User): Flow<Unit>
    fun login(user: User): Flow<User>
    fun autoLogin(key: String): Flow<User>
    fun googleLogin(email: String, name: String): Flow<User>
    suspend fun forgotPassword(email: String)
//    fun getAllRecipe(email: String): Flow<List<Recipe>>
//    fun getAllIngredients(email: String): Flow<List<Ingredient>>
}