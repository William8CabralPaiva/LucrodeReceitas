package com.cabral.recipe.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.IngredientCosts
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.usecase.CostRecipeUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class RecipeCostsViewModel(
    private val costRecipeUseCase: CostRecipeUseCase,
) : ViewModel() {

    lateinit var recipe: Recipe

    private val _notifyStartLoading = MutableLiveData<Unit>()
    val notifyStartLoading: LiveData<Unit> = _notifyStartLoading

    private val _notifyTitle = MutableLiveData<String>()
    val notifyTitle: LiveData<String> = _notifyTitle

    private val _notifyStopLoading = MutableLiveData<Unit>()
    val notifyStopLoading: LiveData<Unit> = _notifyStopLoading

    private val _notifyIngredients = MutableLiveData<List<IngredientCosts>>()
    val notifyIngredients: LiveData<List<IngredientCosts>> = _notifyIngredients

    private val _notifyPrices = MutableLiveData<Pair<Float?, Float?>>()
    val notifyPrices: LiveData<Pair<Float?, Float?>> = _notifyPrices

    fun load() {
        costRecipeUseCase(recipe)
            .onStart { _notifyStartLoading.postValue(Unit) }
            .catch {
                Log.e("testando custo", "erro")//erro internet
            }.onEach { costs ->
                costs.name?.let {
                    _notifyTitle.postValue(it)
                }
                costs.ingredientList?.let {
                    _notifyIngredients.postValue(it)
                }
                _notifyPrices.postValue(costs.profitPrice to costs.total)
            }.onCompletion {
                _notifyStopLoading.postValue(Unit)
            }
            .launchIn(viewModelScope)
    }
}