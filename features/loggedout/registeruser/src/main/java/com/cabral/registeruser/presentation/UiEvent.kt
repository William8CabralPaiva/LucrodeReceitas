package com.cabral.registeruser.presentation

sealed interface UiEvent {
    data object Success : UiEvent
    data object Error : UiEvent
    data object StartLoading : UiEvent
}