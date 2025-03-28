package com.cabral.hubsrc.source.remote.services

import com.cabral.arch.extensions.GenericThrowable
import com.cabral.arch.extensions.RecipeThrowable
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

class RecipeServicesImpl(
    private val db: FirebaseFirestore,
    private val ingredientServices: IngredientServices
) : RecipeServices {
    override fun getAllRecipes(): Flow<List<RecipeProfitPrice>> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val result = db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES)
            .orderBy(DBConstants.NAME).get().await()
        val recipes = result.toObjects(RecipeRegister::class.java).mapIndexed { index, recipe ->
            recipe.toRecipe(index)
        }

        ingredientServices.getAllIngredients().collect {
            emit(recipes.toListRecipeProfitPrice(it))
        }
    }

    override fun getRecipeByKeyDocument(keyDocument: String): Flow<Recipe> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val result = db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES)
            .whereEqualTo(DBConstants.KEY_DOCUMENT, keyDocument)
            .limit(1)
            .get().await()

        val recipe = result.toObjects(RecipeRegister::class.java).mapIndexed { index, recipe ->
            recipe.toRecipe(index)
        }.firstOrNull()


        if (recipe == null) {
            throw RecipeThrowable.RecipeNotFoundThrowable()
        } else {
            emit(recipe)
        }

    }

    override fun addRecipe(recipe: Recipe): Flow<String?> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val collectionReference =
            db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES)

        val id = recipe.keyDocument ?: collectionReference.document().id

        val newDoc = collectionReference.document(id)

        newDoc.set(recipe.toRecipeRegister(id)).await()
        emit(id)
    }

    override fun deleteRecipe(keyDocument: String): Flow<Unit> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES)
            .document(keyDocument).delete().await()
        emit(Unit)
    }
}