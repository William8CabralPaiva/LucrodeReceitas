package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GoogleLoginUseCase(private val repository: UserRepository) {

    operator fun invoke(email: String, name: String): Flow<User> {
        return repository.googleLogin(email, name)
    }
}