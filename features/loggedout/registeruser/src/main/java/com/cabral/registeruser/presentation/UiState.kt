package com.cabral.registeruser.presentation

sealed interface UiState {
    data object DefaultFieldsState : UiState
    data class ErrorUsername(val message: String) : UiState
    data class ErrorEmail(val message: String) : UiState
    data class ErrorPassword(val message: String) : UiState
    data class ErrorConfirmPassword(val message: String) : UiState
}