package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.repository.UserRepository

class ForgotPasswordUseCase(private val repository: UserRepository) {

    suspend operator fun invoke(email: String) {
        repository.forgotPassword(email)
    }
}