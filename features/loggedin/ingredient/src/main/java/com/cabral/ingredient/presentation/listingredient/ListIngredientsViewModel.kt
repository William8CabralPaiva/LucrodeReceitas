package com.cabral.ingredient.presentation.listingredient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.usecase.DeleteIngredientUseCase
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class ListIngredientsViewModel(
    private val listIngredientUseCase: ListIngredientUseCase,
    private val removeIngredientUseCase: DeleteIngredientUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.StartLoading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var listIngredient = mutableListOf<Ingredient?>()
        private set

    fun getAllIngredients() {
        listIngredientUseCase()
            .onStart { _uiState.emit(UiState.StartLoading) }
            .catch {
                _uiState.emit(UiState.EmptyList)
            }.onEach {
                if (it.isNotEmpty()) {
                    listIngredient = it.toMutableList()
                    _uiState.emit(UiState.ListIngredient(it))
                } else {
                    _uiState.emit(UiState.EmptyList)
                }
            }
            .launchIn(viewModelScope)
    }

    fun deleteIngredient(ingredient: Ingredient) {
        removeIngredientUseCase(ingredient)
            .catch {
                ingredient.name?.let {
                    _uiEvent.emit(UiEvent.ErrorRemoveIngredient(it))
                }
            }.onEach {
                _uiEvent.emit(UiEvent.SuccessRemoveIngredient(ingredient))
                if (listIngredient.isEmpty()) {
                    _uiState.emit(UiState.EmptyList)
                }
            }
            .launchIn(viewModelScope)
    }

}