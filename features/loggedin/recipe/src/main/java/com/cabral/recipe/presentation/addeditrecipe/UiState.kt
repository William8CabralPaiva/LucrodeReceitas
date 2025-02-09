package com.cabral.recipe.presentation.addeditrecipe

import com.cabral.core.common.domain.model.Ingredient

sealed interface UiState {
    data object Default : UiState
    data class Error(val message: String?) : UiState
    data object ErrorSpinner : UiState
    data object DisableSpinner : UiState
    data class RemoveIngredient(val id: Int) : UiState
    data class SuccessEdit(val position: Int) : UiState
    data class EditMode(val editMode: Boolean) : UiState
    data class AddIngredient(val ingredient: Ingredient?) : UiState
    data class ListIngredient(val list: List<Ingredient?>) : UiState
}