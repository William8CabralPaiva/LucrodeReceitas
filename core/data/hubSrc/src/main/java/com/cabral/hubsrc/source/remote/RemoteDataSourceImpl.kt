package com.cabral.hubsrc.source.remote

import android.util.Log
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.User
import com.cabral.remote.local.RemoteDataSource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class RemoteDataSourceImpl(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSource {
    override fun addUser(user: User): Flow<Unit> = flow {
        //somente um emit
        emit(db.collection("user").document().set(user) as Unit)
    }

    override suspend fun addUser2(user: User) {
        withContext(dispatcher) {
            db.collection("user").whereEqualTo("email",user.email).whereEqualTo("password",user.password).get()
                .addOnFailureListener {  Log.i("resposta","errou") }
                .addOnSuccessListener { Log.i("resposta","achou")
            }
            db.collection("user").document().set(user)
        }
    }

    override fun login(email: String): Flow<User> {
        TODO("Not yet implemented")
    }

    override fun getAllRecipe(email: String): Flow<List<Recipe>> {
        TODO("Not yet implemented")
    }

    override fun getAllIngredients(email: String): Flow<List<Ingredient>> {
        TODO("Not yet implemented")
//        val list = emptyList<Ingredient>().toMutableList()
//
//        db.collection("ingredients")
//            .get()
//            .addOnSuccessListener { result ->
//                for (document in result) {
//                    list.add(result as Ingredient)
//                }
//
//            }

    }
}