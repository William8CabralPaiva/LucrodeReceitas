package com.cabral.recipe.presentation.listrecipe

import com.cabral.core.common.domain.model.RecipeProfitPrice

sealed interface UiState {
    data object StartLoading : UiState
    data object EmptyList : UiState
    data class ListRecipe(val listRecipe: List<RecipeProfitPrice>) : UiState
    data class SuccessDelete(val recipeProfitPrice: RecipeProfitPrice) : UiState
}
