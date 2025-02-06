package com.cabral.registeruser.presentation

sealed interface UiEvent {
    data object Success : UiEvent
    data object Error : UiEvent
    data object StartLoading : UiEvent
    data class ErrorUsername(val message: String) : UiEvent
    data class ErrorEmail(val message: String) : UiEvent
    data class ErrorPassword(val message: String) : UiEvent
    data class ErrorConfirmPassword(val message: String) : UiEvent
}