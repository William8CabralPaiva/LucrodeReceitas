package com.cabral.hubsrc.source.remote

import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.model.UserRegister
import com.cabral.remote.local.RemoteDataSource
import com.google.firebase.auth.FirebaseAuth
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

class RemoteDataSourceImpl(
    private val db: FirebaseFirestore = Firebase.firestore,
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : RemoteDataSource {
    override fun addUser(user: User): Flow<Unit> = flow {

        val result = db.collection("user").whereEqualTo("email", user.email).get().await()

        if (result.documents.isEmpty()) {
            val collection = db.collection("user")
            val newDocument = collection.document()
            val generatedId = newDocument.id
            db.collection("user").document(generatedId).set(user)

            val addUser = db.collection("user").document(generatedId).get().await()

            addUser.also {
                if (!user.email.isNullOrEmpty()) {
                    auth.createUserWithEmailAndPassword(user.email!!, user.password!!).await()
                        .also {
                            if (!it!!.user?.email.isNullOrEmpty()) {
                                emit(Unit)
                            } else {
                                throw UserThrowable.AddUserErrorThrowable()
                            }
                        }
                } else {
                    throw UserThrowable.AddUserErrorThrowable()
                }
            }
        } else {
            throw UserThrowable.UserAlreadyRegisterPasswordThrowable()
        }


    }.flowOn(dispatcher)

//    override suspend fun addUser2(user: User) {
//        withContext(dispatcher) {
//            db.collection("user").whereEqualTo("email", user.email)
//                .whereEqualTo("password", user.password).get()
//                .addOnFailureListener {
//                    throw (UserThrowable.AddUserErrorThrowable())
//                }
//                .addOnSuccessListener {
//                    if (it.size() == 0) {
//                        db.collection("user").document().set(user)
//                    } else {
//                        throw (UserThrowable.AddUserThrowable())
//                    }
//                }
//
//        }
//    }

    override fun login(user: User): Flow<User> = flow {
        val signIn =
            auth.signInWithEmailAndPassword(user.email!!, user.password!!).await()//TODO TIRAR !!
        if (signIn.user != null) {
            val result = db.collection("user").whereEqualTo("email", user.email)
                .whereEqualTo("password", user.password).get().await()

            if (result.documents.isNotEmpty()) {
                for (document in result.documents) {
                    user.key = document.id
                }

                if (!user.key.isNullOrEmpty()) {
                    emit(user)
                }
            } else {
                throw UserThrowable.UnknownUserThrowable()
            }
        } else {
            throw UserThrowable.UnknownUserThrowable()

        }

    }.flowOn(dispatcher)

    override fun autoLogin(key: String): Flow<User> = flow<User> {
        val user = User()
        val signIn =
            auth.signInWithEmailAndPassword(user.email!!, user.password!!).await()//TODO TIRAR !!
        if (signIn.user != null) {
            val result = db.collection("user").document(key).get().await()
            result.getByUserKey(user).also {
                if (!user.email.isNullOrEmpty()) {
                    user.key = key
                    emit(user)
                } else {
                    throw UserThrowable.UnknownUserThrowable()
                }
            }
        } else {
            throw UserThrowable.UnknownUserThrowable()
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
            val userRegister = UserRegister(name = name, email = email)
            val collection = db.collection("user")
            val newDocument = collection.document()
            val generatedId = newDocument.id

            collection.document(generatedId).set(userRegister).await().also {
                val _user = User(userRegister.email, key = generatedId)
                emit(_user)
            }
        }

    }.flowOn(dispatcher)

    override suspend fun forgotPassword(email: String) {
        withContext(dispatcher) {
            auth.sendPasswordResetEmail(email).await()
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

    override fun getAllRecipe(email: String): Flow<List<Recipe>>  {
        TODO()
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