package com.cabral.hubsrc.source.remote

import com.cabral.arch.extensions.RecipeThrowable
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.User
import com.cabral.remote.local.RemoteDataSource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class RemoteDataSourceImpl(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSource {
    override fun addUser(user: User): Flow<Unit> = flow {
        //somente um emit
        db.collection("user").document().set(user)
        emit(Unit)
    }.flowOn(dispatcher)

    override suspend fun addUser2(user: User) {
        withContext(dispatcher) {
            db.collection("user").whereEqualTo("email", user.email)
                .whereEqualTo("password", user.password).get()
                .addOnFailureListener {
                    throw (RecipeThrowable.AddUserThrowableError())
                }
                .addOnSuccessListener {
                    if (it.size() == 0) {
                        db.collection("user").document().set(user)
                    } else {
                        throw (RecipeThrowable.AddUserThrowable())
                    }
                }

        }
    }

    override fun login(user: User): Flow<User> = flow {
//        db.collection("user").whereEqualTo("email", user.email)
//            .whereEqualTo("password", user.password).get().addOnSuccessListener {
//                for (document in it) {
//                    user.key = document.id
//                }
//            }.await().also {
//                if (!user.key.isNullOrEmpty()) {
//                    emit(user)
//                } else {
//                    throw RecipeThrowable.UnknownUserThrowable()
//                }
//            }

        val result = db.collection("user").whereEqualTo("email", user.email)
            .whereEqualTo("password", user.password).get().await()

        if (result.documents.isNotEmpty()) {
            for (document in result.documents) {
                user.key = document.id
            }

            if (!user.key.isNullOrEmpty()) {
                emit(user)
            }
        }
        throw RecipeThrowable.UnknownUserThrowable()


    }.flowOn(dispatcher)

    override fun autoLogin(key: String): Flow<User> = flow<User> {
        val user = User()
        val result = db.collection("user").document(key).get().await()
        result.getByUserKey(user).also {
            if (!user.email.isNullOrEmpty()) {
                user.key = key
                emit(user)
            } else {
                throw RecipeThrowable.UnknownUserThrowable()
            }
        }
    }.flowOn(dispatcher)

    override fun googleLogin(email: String, name: String): Flow<User> = flow {
        val user = User()
        val result = db.collection("user").whereEqualTo("email", email).get().await()

        if (result.documents.isNotEmpty()) {
            for (document in result.documents) {
                user.key = document.id
            }

            if (!user.key.isNullOrEmpty()) {
                emit(user)
            }
        } else {
            val userRegister = UserRegister(email, name, null)
            val collection = db.collection("user")
            val newDocument = collection.document()
            val generatedId = newDocument.id

            collection.document(generatedId).set(userRegister).await().also {
                val _user = User(userRegister.email, key = generatedId)
                emit(_user)
            }
        }

    }

    private fun DocumentSnapshot.getByUserKey(
        user: User
    ) {
        data?.forEach { map ->
            when (map.key) {
                "name" -> user.name = map.value as String
                "email" -> user.email = map.value as String
                "password" -> user.password = map.value as String
            }
        }
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

    data class UserRegister(
        val email: String,
        val name: String?,
        val password: String?,
    )
}