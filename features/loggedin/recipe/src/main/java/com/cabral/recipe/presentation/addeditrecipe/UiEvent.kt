package com.cabral.recipe.presentation.addeditrecipe

sealed interface UiEvent {
    data class ShowToast(val text: Int) : UiEvent
}