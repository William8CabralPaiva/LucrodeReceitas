package com.cabral.recipe.presentation.recipe

sealed interface UiEvent {
    data object StartLoading : UiEvent
    data object StopLoading : UiEvent
    data object Success : UiEvent
    data class Error(val message: String? = null) : UiEvent
}