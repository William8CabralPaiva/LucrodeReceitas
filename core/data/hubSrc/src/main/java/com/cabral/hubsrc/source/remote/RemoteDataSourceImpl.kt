package com.cabral.hubsrc.source.remote

import com.cabral.arch.extensions.GenericThrowable
import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.IngredientRegister
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeRegister
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.model.UserRegister
import com.cabral.core.common.domain.model.toIngredient
import com.cabral.core.common.domain.model.toIngredientRegister
import com.cabral.core.common.domain.model.toRecipe
import com.cabral.core.common.domain.model.toRecipeRegister
import com.cabral.core.common.domain.model.toUserRegister
import com.cabral.hubsrc.source.DBConstants
import com.cabral.remote.local.RemoteDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
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
        user.name?.let { name ->
            user.email?.let { email ->
                val userRegister = user.toUserRegister()
                val result =
                    db.collection(DBConstants.USER).whereEqualTo(DBConstants.EMAIL, email).get()
                        .await()

                if (result.documents.isEmpty()) {
                    val collection = db.collection(DBConstants.USER)
                    val newDocument = collection.document()
                    val generatedId = newDocument.id
                    db.collection(DBConstants.USER).document(generatedId).set(userRegister)

                    val addUser =
                        db.collection(DBConstants.USER).document(generatedId).get().await()

                    addUser.also {
                        if (!user.email.isNullOrEmpty()) {
                            user.password?.let { password ->
                                userRegister.email?.let { userEmail ->
                                    auth.createUserWithEmailAndPassword(
                                        userEmail,
                                        password
                                    ).await()
                                        .also {
                                            if (!it.user?.email.isNullOrEmpty()) {
                                                emit(Unit)
                                            } else {
                                                throw UserThrowable.AddUserErrorThrowable()
                                            }
                                        }
                                }
                            }
                        } else {
                            throw UserThrowable.AddUserErrorThrowable()
                        }
                    }
                } else {
                    throw UserThrowable.UserAlreadyRegisterPasswordThrowable()
                }
            }
        }

    }.flowOn(dispatcher)

//    override suspend fun addUser2(user: User) {
//        withContext(dispatcher) {
//            db.collection(DBConstants.user).whereEqualTo(DBConstants.EMAIL, user.email)
//                .whereEqualTo(DBConstants.PASSWORD, user.password).get()
//                .addOnFailureListener {
//                    throw (UserThrowable.AddUserErrorThrowable())
//                }
//                .addOnSuccessListener {
//                    if (it.size() == 0) {
//                        db.collection(DBConstants.user).document().set(user)
//                    } else {
//                        throw (UserThrowable.AddUserThrowable())
//                    }
//                }
//
//        }
//    }

    override fun login(user: User): Flow<User> = flow {
        user.email?.let { email ->
            user.password?.let { password ->
                val signIn =
                    auth.signInWithEmailAndPassword(email, password).await()
                if (signIn.user != null) {
                    val result =
                        db.collection(DBConstants.USER).whereEqualTo(DBConstants.EMAIL, user.email)
                            .get().await()

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
            }
        }

    }.flowOn(dispatcher)

    override fun autoLogin(key: String): Flow<User> = flow<User> {
        val user = User()
        val result = db.collection(DBConstants.USER).document(key).get().await()
        result.getByUserKey(user).also {
            if (!user.email.isNullOrEmpty()) {
                user.key = key
                user.email?.let { email ->
                    user.password?.let { password ->
                        val signIn =
                            auth.signInWithEmailAndPassword(email, password).await()
                        if (signIn.user != null) {
                            emit(user)
                        } else {
                            throw UserThrowable.UnknownUserThrowable()
                        }
                        emit(user)
                    }
                }
            } else {
                throw UserThrowable.UnknownUserThrowable()
            }
        }

//        val user = User()
//        val signIn =
//            auth.signInWithEmailAndPassword(user.email, user.password).await()
//        if (signIn.user != null) {
//            val result = db.collection(DBConstants.user).document(key).get().await()
//            result.getByUserKey(user).also {
//                if (!user.email.isNullOrEmpty()) {
//                    user.key = key
//                    emit(user)
//                } else {
//                    throw UserThrowable.UnknownUserThrowable()
//                }
//            }
//        } else {
//            throw UserThrowable.UnknownUserThrowable()
//        }

    }.flowOn(dispatcher)

    override fun googleLogin(email: String, name: String): Flow<User> = flow {
        val user = User()
        val result =
            db.collection(DBConstants.USER).whereEqualTo(DBConstants.EMAIL, email).get().await()

        if (result.documents.isNotEmpty()) {
            for (document in result.documents) {
                user.key = document.id
            }

            if (!user.key.isNullOrEmpty()) {
                emit(user)
            }
        } else {
            val userRegister = UserRegister(name = name, email = email)
            val collection = db.collection(DBConstants.USER)
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
                DBConstants.NAME -> user.name = map.value as String
                DBConstants.EMAIL -> user.email = map.value as String
                DBConstants.PASSWORD -> user.password = map.value as String
            }
        }
    }

    private fun DocumentSnapshot.convertToIngredientRegister(
        id: Int
    ): Ingredient {
        val ingredient = Ingredient(0)

        data?.forEach { map ->
            when (map.key) {
                DBConstants.ID -> ingredient.id = id
                DBConstants.NAME -> ingredient.name = map.value as String?
                DBConstants.VOLUME -> ingredient.volume = map.value as Float?
                DBConstants.UNIT -> ingredient.unit = map.value as String?
                DBConstants.PRICE -> ingredient.price = map.value as Float?
                DBConstants.KEY_DOCUMENT -> ingredient.keyDocument = map.value as String?
            }
        }
        return ingredient
    }

    override fun getAllRecipe(): Flow<List<Recipe>> = flow {
        SingletonUser.getInstance().getKey()?.let { key ->
            val query =
                db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES).get()
            query.await()
            val list = mutableListOf<Recipe>()

            if (query.isSuccessful) {
                var id = 0;
                for (document in query.result.documents) {
                    val auxRecipe = document.toObject(RecipeRegister::class.java)

                    val recipe = auxRecipe?.toRecipe(id)

                    recipe?.let {
                        list.add(it)
                        id += 1
                    }
                }
                emit(list)
            } else {
                throw GenericThrowable.FailThrowable()
            }
        }

    }.flowOn(dispatcher)

    override fun addRecipe(recipe: Recipe): Flow<String?> = flow {

        SingletonUser.getInstance().getKey()?.let { key ->
            val newDocument =
                db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES)
                    .document()

            val id = if (recipe.keyDocument != null) {
                recipe.keyDocument
            } else {
                newDocument.id
            }

            id?.let {
                val recipeRegister = recipe.toRecipeRegister(id)

                db.collection(DBConstants.USER).document(key).collection(DBConstants.RECIPES)
                    .document(id)
                    .set(recipeRegister)

                val getAddRecipe = newDocument.get()
                getAddRecipe.await()
                if (getAddRecipe.isSuccessful) {
                    emit(id)
                } else {
                    throw GenericThrowable.FailThrowable()
                }
            }

        }

    }.flowOn(dispatcher)

    override fun deleteRecipe(keyDocument: String) = flow {
        SingletonUser.getInstance().getKey()?.let { key ->
            val document = db.collection(DBConstants.USER).document(key)
                .collection(DBConstants.RECIPES)
                .document(keyDocument)

            document.delete().await()

            val result = document.get().await()

            if (!result.exists()) {
                emit(Unit)
            } else {
                GenericThrowable.FailThrowable()
            }


        }

    }.flowOn(dispatcher)

    override fun getAllIngredients(): Flow<List<Ingredient>> = flow<List<Ingredient>> {
        SingletonUser.getInstance().getKey()?.let { key ->
            val query =
                db.collection(DBConstants.USER).document(key).collection(DBConstants.INGREDIENTS)
                    .get()
            query.await()
            val list = mutableListOf<Ingredient>()

            if (query.isSuccessful) {
                var id = 0;
                for (document in query.result.documents) {
                    val auxIngredient = document.toObject(IngredientRegister::class.java)

                    val ingredient = auxIngredient?.toIngredient(id)
                    ingredient?.let {
                        list.add(it)
                        id += 1
                    }
                }
                emit(list)
            } else {
                throw GenericThrowable.FailThrowable()
            }
        }

    }.flowOn(dispatcher)

    override fun addIngredient(listIngredient: List<Ingredient>): Flow<Unit> = flow {

        SingletonUser.getInstance().getKey()?.let { key ->
            val batch = db.batch()

            val documentReferences = mutableListOf<DocumentReference>()

            listIngredient.forEach {

                val document = it.keyDocument?.let {
                    db.collection(DBConstants.USER).document(key)
                        .collection(DBConstants.INGREDIENTS).document(it)
                } ?: run {
                    db.collection(DBConstants.USER).document(key)
                        .collection(DBConstants.INGREDIENTS).document()
                }

                it.keyDocument = document.id
                documentReferences.add(document)
                batch.set(document, it.toIngredientRegister())
            }
            batch.commit().await()

        }

        emit(Unit)
    }.flowOn(dispatcher)

    override fun deleteIngredient(ingredient: Ingredient): Flow<Unit> = flow {
        SingletonUser.getInstance().getKey()?.let { key ->
            ingredient.keyDocument?.let { keyDocument ->
                val document = db.collection(DBConstants.USER).document(key)
                    .collection(DBConstants.INGREDIENTS)
                    .document(keyDocument)

                document.delete().await()

                val result = document.get().await()

                if (!result.exists()) {
                    deleteEachItemIngredientList(key, ingredient).also {
                        emit(Unit)
                    }

                } else {
                    GenericThrowable.FailThrowable()
                }
            }

        }

    }.flowOn(dispatcher)

    private suspend fun deleteEachItemIngredientList(
        key: String,
        ingredient: Ingredient
    ) {
        val query =
            db.collection(DBConstants.USER).document(key)
                .collection(DBConstants.RECIPES).get()
        query.await()

        if (query.isSuccessful) {
            for (documentRecipe in query.result.documents) {
                val recipeRegister = documentRecipe.toObject(RecipeRegister::class.java)

                val listUpdated =
                    recipeRegister?.ingredientList?.filter { it.keyDocument != ingredient.keyDocument }

                documentRecipe.reference.update(DBConstants.LIST_INGREDIENTS, listUpdated)
                    .await()
            }
        }
    }

    override fun changeIngredient(ingredient: Ingredient): Flow<Unit> {
        TODO("Not yet implemented")
    }
}