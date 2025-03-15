package com.cabral.hubsrc.source.remote.services

import com.cabral.core.common.domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface UserServices {
    fun addUser(user: User): Flow<Unit> = flow {}

    fun login(user: User): Flow<User> = flow {}

    fun autoLogin(key: String): Flow<User> = flow {}

    fun googleLogin(email: String, name: String): Flow<User> = flow {}

    suspend fun forgotPassword(email: String) {}
}