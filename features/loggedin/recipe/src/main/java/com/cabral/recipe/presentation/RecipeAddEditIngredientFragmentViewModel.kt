package com.cabral.recipe.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.extensions.removeEndZero
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import com.cabral.recipe.R
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import com.cabral.recipe.R as RecipeR

class RecipeAddEditIngredientFragmentViewModel(
    private val listIngredientUseCase: ListIngredientUseCase,
) : ViewModel() {

    private val _notifyStartLoading = MutableLiveData<Unit>()
    val notifyStartLoading: LiveData<Unit> = _notifyStartLoading

    private val _notifyListIngredient = MutableLiveData<List<Ingredient?>>()
    val notifyListIngredient: LiveData<List<Ingredient?>> = _notifyListIngredient

    private val _notifyEmptyList = MutableLiveData<Unit>()
    val notifyEmptyList: LiveData<Unit> = _notifyEmptyList

    private val _notifyRemoveIngredient = MutableLiveData<Int>()
    val notifyRemoveIngredient: LiveData<Int> = _notifyRemoveIngredient

    private val _notifySuccessRemove = MutableLiveData<Ingredient>()
    val notifySuccessRemove: LiveData<Ingredient> = _notifySuccessRemove

    private val _notifySuccessEdit = MutableLiveData<Int>()
    val notifySuccessEdit: LiveData<Int> = _notifySuccessEdit

    private val _notifyError = MutableLiveData<String>()
    val notifyError: LiveData<String> = _notifyError

    private val _notifyShowToast = MutableLiveData<Int>()
    val notifyShowToast: LiveData<Int> = _notifyShowToast

    private val _notifyAddIngredient = MutableLiveData<Ingredient?>()
    val notifyAddIngredient: LiveData<Ingredient?> = _notifyAddIngredient

    private val _notifyEditMode = MutableLiveData<Boolean>()
    val notifyEditMode: LiveData<Boolean> = _notifyEditMode

    var listIngredient = mutableListOf<Ingredient?>()

    var addListIngredient = mutableListOf<Ingredient?>()

    private var editMode = false

    var editPosition: Int? = null

    fun setEditMode(_editMode: Boolean, ingredient: Ingredient?) {
        ingredient?.keyDocument?.let {
            editPosition = addListIngredient.getPosition(it)
        } ?: run {
            null
        }
        editMode = _editMode
        _notifyEditMode.postValue(editMode)
    }

    fun getEditMode(): Boolean {
        return editMode
    }

    init {
        getAllIngredients()
    }

    fun getAllIngredients() {
        listIngredientUseCase()
            .onStart { _notifyStartLoading.postValue(Unit) }
            .catch {
                _notifyEmptyList.postValue(Unit)
            }.onEach {
                if (it.isNotEmpty()) {
                    listIngredient = it.convertToGOrMl()
                    _notifyListIngredient.postValue(listIngredient)
                } else {
                    _notifyEmptyList.postValue(Unit)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun List<Ingredient>.convertToGOrMl(): MutableList<Ingredient?> {
        val list = mutableListOf<Ingredient?>()
        forEach {
            when (it.unit) {
                UnitMeasureType.KG.unit -> {
                    it.unit = UnitMeasureType.G.unit
                    it.volume = it.volume?.times(1000)
                }

                UnitMeasureType.L.unit -> {
                    it.unit = UnitMeasureType.ML.unit
                    it.volume = it.volume?.times(1000)
                }
            }
            list.add(it)
        }
        return list
    }

    fun addIngredientInList(name: String?, volume: Float?) {
        if (editMode && name != null && volume != null) {
            editPosition?.let {
                addListIngredient[it]?.run {
                    this.name = name
                    this.volume = volume
                }
                editMode = false
                _notifyEditMode.postValue(false)
                _notifySuccessEdit.postValue(addListIngredient[it]?.id)
            }
        } else {
            val selectedIngredient =
                getSelectedIngredient(name)
            if (selectedIngredient != null &&
                !addListIngredient.contains(selectedIngredient)
            ) {
                addListIngredient.add(selectedIngredient)
                _notifyAddIngredient.postValue(selectedIngredient)
            } else {
                _notifyShowToast.postValue(RecipeR.string.recipe_item_already_add)
            }
        }
    }

    fun deleteItemAdd(ingredient: Ingredient?) {
        ingredient?.keyDocument?.let {
            val position = addListIngredient.getPosition(it)
            addListIngredient.remove(addListIngredient[position])
            _notifyRemoveIngredient.postValue(ingredient.id)
        }
    }

    private fun getSelectedIngredient(name: String?): Ingredient? {
        return try {
            listIngredient.first { ingredient ->
                ingredient?.name == name
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun MutableList<Ingredient?>.getPosition(keyDocument: String): Int {
        return indexOfFirst { it?.keyDocument == keyDocument }
    }
}
