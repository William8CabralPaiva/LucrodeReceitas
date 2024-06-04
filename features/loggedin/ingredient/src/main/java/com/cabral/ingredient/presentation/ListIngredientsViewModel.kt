package com.cabral.ingredient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.Event
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import com.cabral.core.common.domain.usecase.DeleteIngredientUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class ListIngredientsViewModel(
    private val listIngredientUseCase: ListIngredientUseCase,
    private val removeIngredientUseCase: DeleteIngredientUseCase
) : ViewModel() {

    private val _notifyStartLoading = MutableLiveData<Event<Unit>>()
    val notifyStartLoading: LiveData<Event<Unit>> = _notifyStartLoading

    private val _notifyListIngredient = MutableLiveData<List<Ingredient>>()
    val notifyListIngredient: LiveData<List<Ingredient>> = _notifyListIngredient

    private val _notifyEmptyList = MutableLiveData<Event<Unit>>()
    val notifyEmptyList: LiveData<Event<Unit>> = _notifyEmptyList

    private val _notifySuccessRemove = MutableLiveData<Event<Ingredient>>()
    val notifySuccessRemove: LiveData<Event<Ingredient>> = _notifySuccessRemove

    private val _notifyErrorRemove = MutableLiveData<Event<String>>()
    val notifyErrorRemove: LiveData<Event<String>> = _notifyErrorRemove

    var listIngredient = mutableListOf<Ingredient?>()

    init {
        getAllIngredients()
    }

    fun getAllIngredients() {
        listIngredientUseCase()
            .onStart { _notifyStartLoading.postValue(Event(Unit)) }
            .catch {
                _notifyEmptyList.postValue(Event(Unit))
            }.onEach {
                if (it.isNotEmpty()) {
                    listIngredient = it.toMutableList()
                    _notifyListIngredient.postValue(it)
                } else {
                    _notifyListIngredient.postValue(mutableListOf())
                }
            }
            .launchIn(viewModelScope)
    }

    fun deleteIngredient(ingredient: Ingredient) {
        removeIngredientUseCase(ingredient)
            .catch {
                ingredient.name?.let {
                    _notifyErrorRemove.postValue(Event(it))
                }
            }.onEach {
                _notifySuccessRemove.postValue(Event(ingredient))
                if (listIngredient.isEmpty()) {
                    _notifyEmptyList.postValue(Event(Unit))
                }
            }
            .launchIn(viewModelScope)
    }

}