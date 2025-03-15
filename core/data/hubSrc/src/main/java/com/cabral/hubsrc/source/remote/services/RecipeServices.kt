package com.cabral.hubsrc.source.remote.services

import com.cabral.arch.extensions.GenericThrowable
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.core.common.domain.model.RecipeRegister
import com.cabral.core.common.domain.model.toListRecipeProfitPrice
import com.cabral.core.common.domain.model.toRecipe
import com.cabral.core.common.domain.model.toRecipeRegister
import com.cabral.hubsrc.source.DBConstants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

interface RecipeServices {
    fun getAllRecipes(): Flow<List<RecipeProfitPrice>> = flow {}

    fun addRecipe(recipe: Recipe): Flow<String?> = flow {}

    fun deleteRecipe(keyDocument: String): Flow<Unit> = flow {}
}