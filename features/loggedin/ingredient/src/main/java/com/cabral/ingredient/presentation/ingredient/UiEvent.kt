package com.cabral.ingredient.presentation.ingredient

sealed interface UiEvent {
    data object Success : UiEvent
    data object Error : UiEvent
}