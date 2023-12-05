package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.LucroReceitaRepository
import kotlinx.coroutines.flow.Flow

class LoginUseCase(private val repository: LucroReceitaRepository) {

    operator fun invoke(user: User): Flow<String> {
        return repository.login(user)
    }
}