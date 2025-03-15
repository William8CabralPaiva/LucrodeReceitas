package com.cabral.hubsrc.source.remote.services

import com.cabral.arch.extensions.GenericThrowable
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.IngredientRegister
import com.cabral.core.common.domain.model.toIngredient
import com.cabral.core.common.domain.model.toIngredientRegister
import com.cabral.hubsrc.source.DBConstants
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class IngredientServicesImpl(private val db: FirebaseFirestore): IngredientServices {
    override fun getAllIngredients(): Flow<List<Ingredient>> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val result = db.collection(DBConstants.USER).document(key).collection(DBConstants.INGREDIENTS).orderBy("name").get().await()
        emit(result.toObjects(IngredientRegister::class.java).mapIndexed { id, it -> it.toIngredient(id) })
    }

    override fun addIngredient(ingredientList: List<Ingredient>): Flow<Unit> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val batch = db.batch()
        ingredientList.forEach {
            val doc = db.collection(DBConstants.USER).document(key).collection(DBConstants.INGREDIENTS).document(it.keyDocument ?: "")
            batch.set(doc, it.toIngredientRegister())
        }
        batch.commit().await()
        emit(Unit)
    }

    override fun deleteIngredient(ingredient: Ingredient): Flow<Unit> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        db.collection(DBConstants.USER).document(key).collection(DBConstants.INGREDIENTS).document(ingredient.keyDocument!!).delete().await()
        emit(Unit)
    }
}