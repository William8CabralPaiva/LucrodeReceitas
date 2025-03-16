package com.cabral.recipe.presentation.recipecosts


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.usecase.CostRecipeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class RecipeCostsViewModel(
    private val costRecipeUseCase: CostRecipeUseCase,
) : ViewModel() {

    lateinit var recipe: Recipe

    private val _uiState = MutableStateFlow<UiState>(UiState.StartLoading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun load() {
        costRecipeUseCase(recipe)
            .onStart { _uiState.emit(UiState.StartLoading) }
            .catch { _uiState.emit(UiState.Error) }
            .onEach { costsRecipe ->
                _uiState.emit(
                    UiState.Success(
                        title = costsRecipe.name.orEmpty(),
                        ingredients = costsRecipe.ingredientList.orEmpty(),
                        costs = costsRecipe.costs ?: 0f,
                        totalPerUnit = costsRecipe.totalPerUnit ?: 0f,
                        profit = costsRecipe.profit ?: 0f,
                        profitPerUnit = if ((costsRecipe.volume
                                ?: 1f) > 1f
                        ) costsRecipe.profitPerUnit ?: 0f else null,
                        costsPerUnit = if ((costsRecipe.volume ?: 1f) > 1f) costsRecipe.costsPerUnit
                            ?: 0f else null
                    )
                )
            }
            .onCompletion { _uiState.emit(UiState.StopLoading) }
            .launchIn(viewModelScope)
    }
}
