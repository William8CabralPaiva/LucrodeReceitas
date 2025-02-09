package com.cabral.recipe.presentation.recipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.extensions.RecipeThrowable
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.usecase.AddRecipeUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val addRecipeUseCase: AddRecipeUseCase
) : ViewModel() {

    private val _uiEvent = MutableSharedFlow<UiEvent>(replay = 0)
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    var recipeAlreadyCreate = false

    var recipe: Recipe = Recipe(-1)

    fun addRecipe(name: String?, volume: Float?, expectedProfit: Float?) {
        if (!name.isNullOrEmpty()) {
            recipe.name = name
            recipe.volume = volume
            recipe.expectedProfit = expectedProfit

            addRecipeUseCase(recipe)
                .onStart {
                    _uiEvent.emit(UiEvent.StartLoading)
                }
                .catch {
                    _uiEvent.emit(UiEvent.Error(it.message))
                }.onEach {
                    recipeAlreadyCreate = true
                    recipe.keyDocument = it
                    _uiEvent.emit(UiEvent.Success)
                }
                .onCompletion { _uiEvent.emit(UiEvent.StopLoading) }
                .launchIn(viewModelScope)

        } else {
            viewModelScope.launch {
                _uiEvent.emit(UiEvent.StopLoading)
                _uiEvent.emit(UiEvent.Error(RecipeThrowable.AddRecipeThrowable().message))
            }
        }
    }

}