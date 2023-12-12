package com.cabral.ingredient.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cabral.arch.extensions.IngredientThrowable
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.UnitMeasureType

class IngredientsViewModel() : ViewModel() {

    val listIngredient = mutableListOf<Ingredient>()

    private val _notifyErrorAdd = MutableLiveData<IngredientThrowable>()
    val notifyErrorAdd: LiveData<IngredientThrowable> = _notifyErrorAdd

    private val _notifySuccessAdd = MutableLiveData<Int>()
    val notifySuccessAdd: LiveData<Int> = _notifySuccessAdd

    private var countItem = 0

    fun addIngredient(name: String?, volume: String?, unit: String?, price: String?) {
        try {
            val ingredient = validateField(name, volume, unit, price)
            if (ingredient != null) {
                listIngredient.add(ingredient)
                _notifySuccessAdd.postValue(ingredient.id)
                countItem += 1
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


        return Ingredient(
            countItem,
            name,
            volume.toFloat(),
            unit,
            price.toFloat()
        )

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