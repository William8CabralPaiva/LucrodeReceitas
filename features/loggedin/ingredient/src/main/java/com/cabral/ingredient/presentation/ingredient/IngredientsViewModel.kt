package com.cabral.ingredient.presentation.ingredient

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.extensions.IngredientThrowable
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.core.common.domain.usecase.AddIngredientUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class IngredientsViewModel(
    private val addIngredientUseCase: AddIngredientUseCase
) : ViewModel() {

    val listIngredient = mutableListOf<Ingredient>()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    private val _uiState = MutableStateFlow<UiState>(UiState.Default)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var countItem = 0

    private var editPosition: Int? = null

    private var editItemOnList = false

    var editExistItem = false

    fun save() {
        addIngredientUseCase(listIngredient).catch {
                _uiEvent.emit(UiEvent.Error)
            }.onEach {
                _uiEvent.emit(UiEvent.Success)
            }.launchIn(viewModelScope)
    }

    fun addOrEditIngredient(name: String?, volume: String?, unit: String?, price: String?) {
        viewModelScope.launch {
            try {
                val ingredient = validateField(name, volume, unit, price)
                if (ingredient != null) {

                    if (editItemOnList) {
                        editPosition?.let {
                            ingredient.id = it
                            listIngredient[it] = ingredient
                        }

                        editPosition?.let {
                            _uiState.emit(UiState.SuccessEdit(it)).also {
                                setEditMode(false, null)
                            }
                        }

                    } else {
                        listIngredient.add(ingredient)
                        _uiState.emit(UiState.SuccessAdd(listIngredient.size - 1))
                        countItem += 1
                    }
                }
            } catch (t: IngredientThrowable) {
                _uiState.emit(UiState.ErrorAddEdit(t))
            }
        }
    }

    private fun validateField(
        name: String?, volume: String?, unit: String?, price: String?
    ): Ingredient {
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
            countItem, name, volume.toFloat(), unit, price.toFloat(), key
        )

    }

    fun setEditMode(editMode: Boolean, ingredient: Ingredient?) {
        viewModelScope.launch {
            editPosition = if (ingredient == null) {
                null
            } else {
                listIngredient.indexOf(ingredient)
            }
            editItemOnList = editMode
            _uiState.emit(UiState.EditMode(editItemOnList))
        }
    }

    fun changeIngredient(ingredient: Ingredient) {
        viewModelScope.launch {
            listIngredient.add(ingredient)
            editPosition = 0
            editItemOnList = true
            _uiState.emit(UiState.EditMode(true))
        }
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