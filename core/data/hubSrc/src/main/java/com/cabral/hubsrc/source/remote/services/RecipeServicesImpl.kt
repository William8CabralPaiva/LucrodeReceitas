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

class RecipeServicesImpl(private val db: FirebaseFirestore):RecipeServices {
    override fun getAllRecipes(): Flow<List<RecipeProfitPrice>> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val result = db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES).orderBy("name").get().await()
        val recipes = result.toObjects(RecipeRegister::class.java).mapIndexed { id, it -> it.toRecipe(id) }
        emit(recipes.toListRecipeProfitPrice(emptyList()))
    }

    override fun addRecipe(recipe: Recipe): Flow<String?> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val newDoc = db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES).document()
        val id = recipe.keyDocument ?: newDoc.id
        newDoc.set(recipe.toRecipeRegister(id)).await()
        emit(id)
    }

    override fun deleteRecipe(keyDocument: String): Flow<Unit> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES).document(keyDocument).delete().await()
        emit(Unit)
    }
}