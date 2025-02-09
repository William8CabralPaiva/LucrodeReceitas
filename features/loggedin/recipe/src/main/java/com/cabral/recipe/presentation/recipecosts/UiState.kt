package com.cabral.recipe.presentation.recipecosts

import com.cabral.core.common.domain.model.IngredientCosts

sealed class UiState {
    data object StartLoading : UiState()
    data object StopLoading : UiState()
    data object Error : UiState()
    data class Success(
        val title: String,
        val ingredients: List<IngredientCosts>,
        val costs: Float,
        val totalPerUnit: Float,
        val profit: Float,
        val profitPerUnit: Float?,
        val costsPerUnit: Float?
    ) : UiState()
}