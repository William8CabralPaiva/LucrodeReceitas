package com.cabral.recipe.presentation.listrecipe

sealed interface UiEvent {
    data class ErrorDelete(val message: String) : UiEvent
}