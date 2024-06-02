package com.cabral.recipe.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.Event
import com.cabral.arch.extensions.RecipeThrowable
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.usecase.AddRecipeUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach

class RecipeViewModel(
    private val addRecipeUseCase: AddRecipeUseCase
) : ViewModel() {

    private val _notifySuccess = MutableLiveData<Event<Unit>>()
    val notifySuccess: LiveData<Event<Unit>> = _notifySuccess

    private val _notifyError = MutableLiveData<Event<String>>()
    val notifyError: LiveData<Event<String>> = _notifyError

    private val _notifyStopLoadingButton = MutableLiveData<Event<Unit>>()
    val notifyStopLoadingButton: LiveData<Event<Unit>> = _notifyStopLoadingButton

    var recipeAlreadyCreate = false

    var recipe: Recipe = Recipe(-1)

    fun addRecipe(name: String?, volume: Float?, expectedProfit: Float?) {
        if (!name.isNullOrEmpty()) {
            recipe.name = name
            recipe.volume = volume
            recipe.expectedProfit = expectedProfit
            addRecipeUseCase(recipe)
                .catch {
                    _notifyError.postValue(Event(it.message))
                    _notifyStopLoadingButton.postValue(Event(Unit))
                }.onEach {
                    recipeAlreadyCreate = true
                    recipe.keyDocument = it

                    _notifySuccess.postValue(Event(Unit))
                }
                .onCompletion { _notifyStopLoadingButton.postValue(Event(Unit)) }
                .launchIn(viewModelScope)
        } else {
            _notifyStopLoadingButton.postValue(Event(Unit))
            _notifyError.postValue(Event(RecipeThrowable.AddRecipeThrowable().message))
        }
    }

}