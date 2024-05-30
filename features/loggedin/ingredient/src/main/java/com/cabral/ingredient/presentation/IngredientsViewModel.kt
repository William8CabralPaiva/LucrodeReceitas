package com.cabral.ingredient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.extensions.IngredientThrowable
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.core.common.domain.usecase.AddIngredientUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class IngredientsViewModel(
    private val addIngredientUseCase: AddIngredientUseCase
) : ViewModel() {

    val listIngredient = mutableListOf<Ingredient>()

    private val _notifyErrorAdd = MutableLiveData<IngredientThrowable>()
    val notifyErrorAdd: LiveData<IngredientThrowable> = _notifyErrorAdd

    private val _notifySuccessAdd = MutableLiveData<Int>()
    val notifySuccessAdd: LiveData<Int> = _notifySuccessAdd

    private val _notifySuccessEdit = MutableLiveData<Int?>()
    val notifySuccessEdit: LiveData<Int?> = _notifySuccessEdit

    private val _notifyEditMode = MutableLiveData<Boolean>()
    val notifyEditMode: LiveData<Boolean> = _notifyEditMode

    private val _notifySuccess = MutableLiveData<Unit>()
    val notifySuccess: LiveData<Unit> = _notifySuccess

    private val _notifyError = MutableLiveData<Unit>()
    val notifyError: LiveData<Unit> = _notifyError

    private var countItem = 0

    private var editPosition: Int? = null

    private var editItemOnList = false

    var editExistItem = false

    fun save() {
        addIngredientUseCase(listIngredient)
            .catch {
                _notifyError.postValue(Unit)
            }.onEach {
                _notifySuccess.postValue(Unit)
            }
            .launchIn(viewModelScope)
    }

    fun addOrEditIngredient(name: String?, volume: String?, unit: String?, price: String?) {
        try {
            val ingredient = validateField(name, volume, unit, price)
            if (ingredient != null) {

                if (editItemOnList) {
                    editPosition?.let {
                        ingredient.id = it
                        listIngredient[it] = ingredient
                    }

                    _notifySuccessEdit.postValue(editPosition).also {
                        setEditMode(false, null)
                    }

                } else {
                    listIngredient.add(ingredient)
                    _notifySuccessAdd.postValue(listIngredient.size - 1)
                    countItem += 1
                }
            }
        } catch (t: IngredientThrowable) {
            _notifyErrorAdd.postValue(t)
        }
    }

    private fun validateField(
        name: String?,
        volume: String?,
        unit: String?,
        price: String?
    ): Ingredient? {
        if (name.isNullOrEmpty()) {
            throw IngredientThrowable.NameThrowable()
        }

        if (volume.isNullOrEmpty()) {
            throw IngredientThrowable.SizeThrowable()
        }

        volume.let {
            if (it.toFloat() <= 0) {
                throw IngredientThrowable.SizeThrowable()
            }
        }

        if (unit.isNullOrEmpty() || validateUnit(unit) == null) {
            throw IngredientThrowable.UnitThrowable()
        }

        if (price.isNullOrEmpty()) {
            throw IngredientThrowable.PriceThrowable()
        }

        price.let {
            if (it.toFloat() <= 0) {
                throw IngredientThrowable.PriceThrowable()
            }
        }

        val key = if (editExistItem && editItemOnList && editPosition == 0) {
            editExistItem = false
            editPosition?.let {
                listIngredient[it].keyDocument
            }
        } else {
            null
        }

        return Ingredient(
            countItem,
            name,
            volume.toFloat(),
            unit,
            price.toFloat(),
            key
        )

    }

    fun setEditMode(editMode: Boolean, ingredient: Ingredient?) {
        editPosition = if (ingredient == null) {
            null
        } else {
            listIngredient.indexOf(ingredient)
        }
        editItemOnList = editMode
        _notifyEditMode.postValue(editItemOnList)
    }

    fun changeIngredient(ingredient: Ingredient) {
        listIngredient.add(ingredient)
        editPosition = 0
        editItemOnList = true
        _notifyEditMode.postValue(true)
    }

    fun getEditMode(): Boolean {
        return editItemOnList
    }

    private fun validateUnit(unit: String?): UnitMeasureType? {
        try {
            unit?.let {
                val selected = UnitMeasureType.values().filter { enum -> enum.unit == unit }
                if (selected.isNotEmpty()) {
                    return selected.first()
                }
            }
        } catch (t: IngredientThrowable) {
            IngredientThrowable.UnitThrowable("Selecione um item corretamente")
        }
        return null
    }
}