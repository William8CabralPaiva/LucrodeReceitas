package com.cabral.ingredient.presentation.ingredient

import com.cabral.arch.extensions.IngredientThrowable

sealed interface UiState {
    data object Default : UiState
    data class SuccessAdd(val position: Int) : UiState
    data class SuccessEdit(val position: Int) : UiState
    data class ErrorAddEdit(val ingredientThrowable: IngredientThrowable) : UiState
    data class EditMode(val editMode: Boolean) : UiState
}