package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class AddUserUseCase(
    private val repository: UserRepository
) {
    operator fun invoke(user: User): Flow<Unit> {
        return repository.addUser(user)
    }

}