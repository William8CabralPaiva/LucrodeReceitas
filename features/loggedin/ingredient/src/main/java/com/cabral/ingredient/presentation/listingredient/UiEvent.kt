package com.cabral.ingredient.presentation.listingredient

import com.cabral.core.common.domain.model.Ingredient

sealed interface UiEvent {
    data class SuccessRemoveIngredient(val ingredient: Ingredient) : UiEvent
    data class ErrorRemoveIngredient(val ingredientName: String) : UiEvent
}