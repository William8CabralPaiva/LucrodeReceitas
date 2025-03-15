package com.cabral.hubsrc.source.remote.services

import com.cabral.arch.extensions.UserThrowable
import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.model.UserRegister
import com.cabral.core.common.domain.model.toUserRegister
import com.cabral.hubsrc.source.DBConstants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserServicesImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserServices {
    override fun addUser(user: User): Flow<Unit> = flow {
        auth.setLanguageCode("pt-BR")
        val userRegister = user.toUserRegister()
        val result =
            db.collection(DBConstants.USER).whereEqualTo(DBConstants.EMAIL, user.email).get()
                .await()
        if (result.isEmpty) {
            val newUserDoc = db.collection(DBConstants.USER).document()
            newUserDoc.set(userRegister).await()
            auth.createUserWithEmailAndPassword(user.email!!, user.password!!)
                .await().user?.sendEmailVerification()?.await()
            emit(Unit)
        } else throw UserThrowable.UserAlreadyRegisterPasswordThrowable()
    }

    override fun login(user: User): Flow<User> = flow {
        auth.setLanguageCode("pt-BR")
        val signIn = auth.signInWithEmailAndPassword(user.email!!, user.password!!).await()
        if (signIn.user?.isEmailVerified == true) {
            val result =
                db.collection(DBConstants.USER).whereEqualTo(DBConstants.EMAIL, user.email).get()
                    .await()
            if (result.documents.isNotEmpty()) {
                user.key = result.documents.first().id
                emit(user)
            } else throw UserThrowable.UnknownUserThrowable()
        } else {
            signIn.user?.sendEmailVerification()?.await()
            throw UserThrowable.CheckEmailThrowable()
        }
    }

    override fun autoLogin(key: String): Flow<User> = flow {
        val result = db.collection(DBConstants.USER).document(key).get().await()
        val user = result.toObject(User::class.java) ?: throw UserThrowable.UnknownUserThrowable()
        user.key = key
        emit(user)
    }

    override fun googleLogin(email: String, name: String): Flow<User> = flow {
        val result =
            db.collection(DBConstants.USER).whereEqualTo(DBConstants.EMAIL, email).get().await()
        val user = if (result.documents.isNotEmpty()) {
            User(email, key = result.documents.first().id)
        } else {
            val userRegister = UserRegister(name = name, email = email)
            val newUserDoc = db.collection(DBConstants.USER).document()
            newUserDoc.set(userRegister).await()
            User(email, key = newUserDoc.id)
        }
        emit(user)
    }

    override suspend fun forgotPassword(email: String) {
        withContext(Dispatchers.IO) { auth.sendPasswordResetEmail(email).await() }
    }
}