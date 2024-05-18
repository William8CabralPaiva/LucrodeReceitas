package com.cabral.recipe.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.Recipe
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

    private val _notifyStartLoading = MutableLiveData<Unit>()
    val notifyStartLoading: LiveData<Unit> = _notifyStartLoading

    private val _notifySuccessDelete = MutableLiveData<Recipe>()
    val notifySuccessDelete: LiveData<Recipe> = _notifySuccessDelete

    private val _notifyErrorDelete = MutableLiveData<String>()
    val notifyErrorDelete: LiveData<String> = _notifyErrorDelete

    private val _notifyListRecipe = MutableLiveData<List<Recipe>>()
    val notifyListRecipe: LiveData<List<Recipe>> = _notifyListRecipe

    private val _notifyEmptyList = MutableLiveData<Unit>()
    val notifyEmptyList: LiveData<Unit> = _notifyEmptyList

    var listRecipe = mutableListOf<Recipe?>()

    init {
        getAllRecipe()
    }

    fun getAllRecipe() {
        getListRecipe()
            .onStart { _notifyStartLoading.postValue(Unit) }
            .catch {
                _notifyEmptyList.postValue(Unit)
            }.onEach {
                if (it.isNotEmpty()) {
                    listRecipe = it.toMutableList()
                    _notifyListRecipe.postValue(it)
                } else {
                    _notifyEmptyList.postValue(Unit)
                }
            }
            .launchIn(viewModelScope)
    }

    fun deleteRecipe(recipe: Recipe) {
        recipe.keyDocument?.let {
            deleteRecipeUseCase(it)
                .catch {
                    recipe.name?.let {
                        _notifyErrorDelete.postValue(it)
                    }
                }.onEach {
                    _notifySuccessDelete.postValue(recipe)
                }
                .launchIn(viewModelScope)
        }
        _notifySuccessDelete.postValue(recipe)

    }
}