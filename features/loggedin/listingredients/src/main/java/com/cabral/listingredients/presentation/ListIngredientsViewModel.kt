package com.cabral.listingredients.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.SingletonUser
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class ListIngredientsViewModel(
    private val listIngredientUseCase: ListIngredientUseCase
) : ViewModel() {

    private val _notifyStartLoading = MutableLiveData<Unit>()
    val notifyStartLoading: LiveData<Unit> = _notifyStartLoading

    private val _notifyListIngredient = MutableLiveData<List<Ingredient>>()
    val notifyListIngredient: LiveData<List<Ingredient>> = _notifyListIngredient

    private val _notifyEmptyList = MutableLiveData<Unit>()
    val notifyEmptyList: LiveData<Unit> = _notifyEmptyList

    private var listIngredient = mutableListOf<Ingredient?>()

    fun getAllIngredients() {
        listIngredientUseCase()
            .onStart { _notifyStartLoading.postValue(Unit) }
            .catch {
                _notifyEmptyList.postValue(Unit)
            }.onEach {
                if (it.isNotEmpty()) {
                    listIngredient = it.toMutableList()
                    _notifyListIngredient.postValue(it)
                } else {
                    _notifyEmptyList.postValue(Unit)
                }
            }
            .launchIn(viewModelScope)
    }

    fun removeIngredientById(id: Int) {
        val ingredient = listIngredient.first { ingredient -> ingredient?.id == id }
        ingredient?.let {
            listIngredient.remove(ingredient)
        }
    }
}