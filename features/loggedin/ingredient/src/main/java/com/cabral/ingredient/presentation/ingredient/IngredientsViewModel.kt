package com.cabral.ingredient.presentation.ingredient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.Event
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

    private val _notifyErrorAdd = MutableLiveData<Event<IngredientThrowable>>()
    val notifyErrorAdd: LiveData<Event<IngredientThrowable>> = _notifyErrorAdd

    private val _notifySuccessAdd = MutableLiveData<Event<Int>>()
    val notifySuccessAdd: LiveData<Event<Int>> = _notifySuccessAdd

    private val _notifySuccessEdit = MutableLiveData<Event<Int?>>()
    val notifySuccessEdit: LiveData<Event<Int?>> = _notifySuccessEdit

    private val _notifyEditMode = MutableLiveData<Event<Boolean>>()
    val notifyEditMode: LiveData<Event<Boolean>> = _notifyEditMode

    private val _notifySuccess = MutableLiveData<Event<Unit>>()
    val notifySuccess: LiveData<Event<Unit>> = _notifySuccess

    private val _notifyError = MutableLiveData<Event<Unit>>()
    val notifyError: LiveData<Event<Unit>> = _notifyError

    private var countItem = 0

    private var editPosition: Int? = null

    private var editItemOnList = false

    var editExistItem = false

    fun save() {
        addIngredientUseCase(listIngredient)
            .catch {
                _notifyError.postValue(Event(Unit))
            }.onEach {
                _notifySuccess.postValue(Event(Unit))
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

                    _notifySuccessEdit.postValue(Event(editPosition)).also {
                        setEditMode(false, null)
                    }

                } else {
                    listIngredient.add(ingredient)
                    _notifySuccessAdd.postValue(Event(listIngredient.size - 1))
                    countItem += 1
                }
            }
        } catch (t: IngredientThrowable) {
            _notifyErrorAdd.postValue(Event(t))
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
        _notifyEditMode.postValue(Event(editItemOnList))
    }

    fun changeIngredient(ingredient: Ingredient) {
        listIngredient.add(ingredient)
        editPosition = 0
        editItemOnList = true
        _notifyEditMode.postValue(Event(true))
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