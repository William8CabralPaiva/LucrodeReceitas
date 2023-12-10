package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class AutoLoginUseCase(private val repository: UserRepository) {

    operator fun invoke(key: String): Flow<User> {
        return repository.autoLogin(key)
    }
}