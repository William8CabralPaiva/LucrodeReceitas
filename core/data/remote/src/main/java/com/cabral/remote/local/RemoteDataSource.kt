package com.cabral.remote.local

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {

    fun addUser(user: User): Flow<Unit>
    suspend fun addUser2(user: User)
    fun login(user: User): Flow<User>
    fun autoLogin(key: String): Flow<User>
    fun googleLogin(email: String, name: String): Flow<User>

    fun getAllRecipe(email: String): Flow<List<Recipe>>
    fun getAllIngredients(email: String): Flow<List<Ingredient>>
}