package com.cabral.hubsrc.source.remote

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.core.common.domain.model.User
import com.cabral.hubsrc.source.remote.services.IngredientServices
import com.cabral.hubsrc.source.remote.services.RecipeServices
import com.cabral.hubsrc.source.remote.services.UserServices
import com.cabral.remote.local.RemoteDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSourceImpl(
    private val userService: UserServices,
    private val recipeService: RecipeServices,
    private val ingredientService: IngredientServices,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSource {
    override fun addUser(user: User): Flow<Unit> = userService.addUser(user).flowOn(dispatcher)
    override fun login(user: User): Flow<User> = userService.login(user).flowOn(dispatcher)
    override fun autoLogin(key: String): Flow<User> = userService.autoLogin(key).flowOn(dispatcher)
    override fun googleLogin(email: String, name: String): Flow<User> =
        userService.googleLogin(email, name).flowOn(dispatcher)

    override suspend fun forgotPassword(email: String) = userService.forgotPassword(email)
    override fun getAllRecipes(): Flow<List<RecipeProfitPrice>> =
        recipeService.getAllRecipes().flowOn(dispatcher)

    override fun getRecipeByKeyDocument(keyDocument: String): Flow<Recipe> =
        recipeService.getRecipeByKeyDocument(keyDocument)

    override fun addRecipe(recipe: Recipe): Flow<String?> =
        recipeService.addRecipe(recipe).flowOn(dispatcher)

    override fun deleteRecipe(keyDocument: String): Flow<Unit> =
        recipeService.deleteRecipe(keyDocument).flowOn(dispatcher)

    override fun getAllIngredients(): Flow<List<Ingredient>> =
        ingredientService.getAllIngredients().flowOn(dispatcher)

    override fun addIngredient(ingredientList: List<Ingredient>): Flow<Unit> =
        ingredientService.addIngredient(ingredientList).flowOn(dispatcher)

    override fun deleteIngredient(ingredient: Ingredient): Flow<Unit> =
        ingredientService.deleteIngredient(ingredient).flowOn(dispatcher)
}