package com.cabral.remote.local

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.User
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    //TODO ALTERAR FLOW UNIT para suspense
    fun addUser(user: User): Flow<Unit>
    suspend fun addUser2(user: User)
    fun login(user: User): Flow<String>
    fun getAllRecipe(email: String): Flow<List<Recipe>>
    fun getAllIngredients(email: String): Flow<List<Ingredient>>
}