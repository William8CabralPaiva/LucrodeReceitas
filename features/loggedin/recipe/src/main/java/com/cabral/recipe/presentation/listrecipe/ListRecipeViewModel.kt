package com.cabral.recipe.presentation.listrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.core.common.domain.usecase.DeleteRecipeUseCase
import com.cabral.core.common.domain.usecase.GetListRecipeUseCase
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

class ListRecipeViewModel(
    private val getListRecipe: GetListRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.StartLoading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    var listRecipe = mutableListOf<RecipeProfitPrice?>()

    init {
        getAllRecipe()
    }

    fun getAllRecipe() {
        getListRecipe().onStart { _uiState.emit(UiState.StartLoading) }
            .catch {
                _uiState.emit(UiState.EmptyList)
            }.onEach {
                if (it.isNotEmpty()) {
                    listRecipe = it.toMutableList()
                    _uiState.emit(UiState.ListRecipe(it))
                } else {
                    _uiState.emit(UiState.EmptyList)
                }
            }.launchIn(viewModelScope)
    }

    fun deleteRecipe(recipeProfitPrice: RecipeProfitPrice) {
        recipeProfitPrice.keyDocument?.let {
            deleteRecipeUseCase(it).catch {
                recipeProfitPrice.name?.let { name ->
                    _uiEvent.emit(UiEvent.ErrorDelete(name))
                }
            }.onEach {
                _uiState.emit(UiState.SuccessDelete(recipeProfitPrice))
            }.launchIn(viewModelScope)
        }

    }
}