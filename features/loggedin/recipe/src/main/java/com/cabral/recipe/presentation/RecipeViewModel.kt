package com.cabral.recipe.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    private val _notifySuccess = MutableLiveData<Unit>()
    val notifySuccess: LiveData<Unit> = _notifySuccess

    private val _notifyError = MutableLiveData<String>()
    val notifyError: LiveData<String> = _notifyError

    private val _notifyStopLoadingButton = MutableLiveData<Unit>()
    val notifyStopLoadingButton: LiveData<Unit> = _notifyStopLoadingButton

    var recipeAlreadyCreate = false

    var recipe: Recipe = Recipe(-1)

    fun addRecipe(name: String?, volume: Float?, expectedProfit: Float?) {
        if (!name.isNullOrEmpty()) {

            recipe.volume = volume
            recipe.expectedProfit = expectedProfit

            addRecipeUseCase(recipe)
                .catch {
                    _notifyError.postValue(it.message)
                }.onEach {
                    recipeAlreadyCreate = true
                    recipe.keyDocument = it
                    _notifySuccess.postValue(Unit)
                }
                .onCompletion { _notifyStopLoadingButton.postValue(Unit) }
                .launchIn(viewModelScope)
        } else {
            _notifyError.postValue(RecipeThrowable.AddRecipeThrowable().message)
        }
    }

}