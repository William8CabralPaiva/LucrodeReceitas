package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.LucroReceitaRepository
import kotlinx.coroutines.flow.Flow

class AddUserUseCase(
    private val repository: LucroReceitaRepository
) {
    operator fun invoke(user: User): Flow<Unit> {
        return repository.addUser(user)
    }

    suspend fun invoke2(user: User) {
        return repository.addUser2(user)
    }

}