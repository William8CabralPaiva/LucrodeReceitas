package com.cabral.ingredient.presentation.listingredient

import com.cabral.core.common.domain.model.Ingredient

sealed interface UiState {
    data object StartLoading : UiState
    data object EmptyList : UiState
    data class ListIngredient(val list: List<Ingredient>) : UiState
}