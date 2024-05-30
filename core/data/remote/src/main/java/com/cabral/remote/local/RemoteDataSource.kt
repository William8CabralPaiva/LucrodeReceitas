package com.cabral.remote.local

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.core.common.domain.model.User
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    fun addUser(user: User): Flow<Unit>
    fun login(user: User): Flow<User>
    fun autoLogin(key: String): Flow<User>
    fun googleLogin(email: String, name: String): Flow<User>
    suspend fun forgotPassword(email: String)
    fun getAllRecipes(): Flow<List<RecipeProfitPrice>>
    fun addRecipe(recipe: Recipe): Flow<String?>
    fun deleteRecipe(keyDocument: String): Flow<Unit>
    fun getAllIngredients(): Flow<List<Ingredient>>
    fun addIngredient(ingredientList: List<Ingredient>): Flow<Unit>
    fun deleteIngredient(ingredient: Ingredient): Flow<Unit>
    fun changeIngredient(ingredient: Ingredient): Flow<Unit>
}