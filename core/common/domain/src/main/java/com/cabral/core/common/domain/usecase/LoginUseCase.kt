package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repository: UserRepository) {

    operator fun invoke(user: User): Flow<User> {
        return repository.login(user)
    }
}