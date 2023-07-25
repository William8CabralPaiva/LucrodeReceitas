package com.cabral.core.common.domain.repository

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LucroReceitaRepository {
     fun addUser(user: User): Flow<Unit>
     suspend fun addUser2(user: User)
     fun login(email: String): Flow<User>
     fun getAllRecipe(email: String): Flow<List<Recipe>>
     fun getAllIngredients(email: String): Flow<List<Ingredient>>
}