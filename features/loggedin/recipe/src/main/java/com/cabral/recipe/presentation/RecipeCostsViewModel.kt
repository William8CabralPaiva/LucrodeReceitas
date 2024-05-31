package com.cabral.recipe.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.IngredientCosts
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeCosts
import com.cabral.core.common.domain.usecase.CostRecipeUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.math.cos

class RecipeCostsViewModel(
    private val costRecipeUseCase: CostRecipeUseCase,
) : ViewModel() {

    lateinit var recipe: Recipe

    private val _notifyStartLoading = MutableLiveData<Unit>()
    val notifyStartLoading: LiveData<Unit> = _notifyStartLoading

    private val _notifyError = MutableLiveData<Unit>()
    val notifyError: LiveData<Unit> = _notifyError

    private val _notifyTitle = MutableLiveData<String>()
    val notifyTitle: LiveData<String> = _notifyTitle

    private val _notifyStopLoading = MutableLiveData<Unit>()
    val notifyStopLoading: LiveData<Unit> = _notifyStopLoading

    private val _notifyIngredients = MutableLiveData<List<IngredientCosts>>()
    val notifyIngredients: LiveData<List<IngredientCosts>> = _notifyIngredients

    private val _notifyTotalPerUnit = MutableLiveData<Float>()
    val notifyTotalPerUnit: LiveData<Float> = _notifyTotalPerUnit

    private val _notifyProfitPerUnit = MutableLiveData<Float>()
    val notifyProfitPerUnit: LiveData<Float> = _notifyProfitPerUnit

    private val _notifyProfit = MutableLiveData<Float>()
    val notifyProfit: LiveData<Float> = _notifyProfit

    private val _notifyCosts = MutableLiveData<Float>()
    val notifyCosts: LiveData<Float> = _notifyCosts

    private val _notifyCostsPerUnit = MutableLiveData<Float>()
    val notifyCostsPerUnit: LiveData<Float> = _notifyCostsPerUnit

    fun load() {
        costRecipeUseCase(recipe)
            .onStart { _notifyStartLoading.postValue(Unit) }
            .catch {
                _notifyError.postValue(Unit)
            }.onEach { costsRecipe ->

                costsRecipe.run {
                    name?.let {
                        _notifyTitle.postValue(it)
                    }
                    ingredientList?.let {
                        _notifyIngredients.postValue(it)
                    }

                    costs.notifyAction(_notifyCosts)
                    totalPerUnit.notifyAction(_notifyTotalPerUnit)
                    profit.notifyAction(_notifyProfit)

                    volume?.let {
                        if (it > 1f) {
                            profitPerUnit.notifyAction(_notifyProfitPerUnit)
                            costsPerUnit.notifyAction(_notifyCostsPerUnit)
                        }
                    }
                }

            }.onCompletion {
                _notifyStopLoading.postValue(Unit)
            }
            .launchIn(viewModelScope)
    }

    private fun Float?.notifyAction(
        abc: MutableLiveData<Float>
    ) {
        this?.let {
            if (it > 0) {
                abc.postValue(it)
            }
        }
    }
}