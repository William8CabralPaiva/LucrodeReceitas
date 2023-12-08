package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.User
import com.cabral.core.common.domain.repository.LucroReceitaRepository
import kotlinx.coroutines.flow.Flow

class AutoLoginUseCase(private val repository: LucroReceitaRepository) {

    operator fun invoke(key: String): Flow<User> {
        return repository.autoLogin(key)
    }
}