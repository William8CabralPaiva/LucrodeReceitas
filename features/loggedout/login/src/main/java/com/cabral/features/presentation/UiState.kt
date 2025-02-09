package com.cabral.features.presentation

import com.cabral.core.common.domain.model.User

sealed interface UiState {
    data object StartLoading : UiState
    data object StopLoading : UiState
    data object GoogleStartLoading : UiState
    data object GoogleStopLoading : UiState
    data class Success(val user: User) : UiState
    data class Error(val message: String) : UiState
    data object ForgotPassword : UiState
    data class ForgotPasswordError(val message: String) : UiState
}