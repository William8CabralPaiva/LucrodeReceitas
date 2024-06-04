package com.cabral.recipe.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.Event
import com.cabral.core.common.domain.model.RecipeProfitPrice
import com.cabral.core.common.domain.usecase.DeleteRecipeUseCase
import com.cabral.core.common.domain.usecase.GetListRecipeUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class ListRecipeViewModel(
    private val getListRecipe: GetListRecipeUseCase,
    private val deleteRecipeUseCase: DeleteRecipeUseCase
) : ViewModel() {

    private val _notifyStartLoading = MutableLiveData<Event<Unit>>()
    val notifyStartLoading: LiveData<Event<Unit>> = _notifyStartLoading

    private val _notifySuccessDelete = MutableLiveData<Event<RecipeProfitPrice>>()
    val notifySuccessDelete: LiveData<Event<RecipeProfitPrice>> = _notifySuccessDelete

    private val _notifyErrorDelete = MutableLiveData<Event<String>>()
    val notifyErrorDelete: LiveData<Event<String>> = _notifyErrorDelete

    private val _notifyListRecipe = MutableLiveData<List<RecipeProfitPrice>>()
    val notifyListRecipe: LiveData<List<RecipeProfitPrice>> = _notifyListRecipe

    private val _notifyEmptyList = MutableLiveData<Event<Unit>>()
    val notifyEmptyList: LiveData<Event<Unit>> = _notifyEmptyList

    var listRecipe = mutableListOf<RecipeProfitPrice?>()

    init {
        getAllRecipe()
    }

    fun getAllRecipe() {
        getListRecipe().onStart { _notifyStartLoading.postValue(Event(Unit)) }
            .catch {
                _notifyEmptyList.postValue(Event(Unit))
            }.onEach {
                if (it.isNotEmpty()) {
                    listRecipe = it.toMutableList()
                    _notifyListRecipe.postValue(it)
                } else {
                    _notifyListRecipe.postValue(mutableListOf())
                }
            }.launchIn(viewModelScope)
    }

    fun deleteRecipe(recipeProfitPrice: RecipeProfitPrice) {
        recipeProfitPrice.keyDocument?.let {
            deleteRecipeUseCase(it).catch {
                recipeProfitPrice.name?.let {
                    _notifyErrorDelete.postValue(Event(it))
                }
            }.onEach {
                _notifySuccessDelete.postValue(Event(recipeProfitPrice))
            }.launchIn(viewModelScope)
        }

    }
}