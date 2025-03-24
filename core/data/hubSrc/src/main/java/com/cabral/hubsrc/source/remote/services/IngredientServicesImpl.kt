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

class IngredientServicesImpl(private val db: FirebaseFirestore) : IngredientServices {
    override fun getAllIngredients(): Flow<List<Ingredient>> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val result =
            db.collection(DBConstants.USER).document(key).collection(DBConstants.INGREDIENTS)
                .orderBy(DBConstants.NAME).get().await()
        emit(
            result.toObjects(IngredientRegister::class.java)
                .mapIndexed { index, ingredient -> ingredient.toIngredient(index) }
        )
    }


    override fun addIngredient(ingredientList: List<Ingredient>): Flow<Unit> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        val batch = db.batch()

        ingredientList.forEach {

            val collectionReference =
                db.collection(DBConstants.USER).document(key).collection(DBConstants.INGREDIENTS)

            val id = it.keyDocument ?: collectionReference.document().id

            val newDoc = collectionReference.document(id)

            batch.set(newDoc, it.toIngredientRegister(id))
        }

        try {
            batch.commit().await()
            emit(Unit)
        } catch (e: Exception) {
            throw GenericThrowable.FailThrowable(e.message.toString())
        }
    }

    override fun deleteIngredient(ingredient: Ingredient): Flow<Unit> = flow {
        val key = SingletonUser.getInstance().getKey() ?: throw GenericThrowable.FailThrowable()
        db.collection(DBConstants.USER).document(key).collection(DBConstants.INGREDIENTS)
            .document(ingredient.keyDocument!!).delete().await()
        emit(Unit)
    }
}