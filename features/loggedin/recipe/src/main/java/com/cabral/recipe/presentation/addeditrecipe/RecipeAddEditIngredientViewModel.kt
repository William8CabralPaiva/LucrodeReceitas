package com.cabral.recipe.presentation.addeditrecipe

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.core.common.domain.model.toIngredientRecipeRegisterList
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
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
import com.cabral.design.R as DesignR

class RecipeAddEditIngredientViewModel(
    private val listIngredientUseCase: ListIngredientUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Default)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UiEvent>()
    val uiEvent: SharedFlow<UiEvent> = _uiEvent.asSharedFlow()

    var listAllIngredients = mutableListOf<Ingredient?>()
    var listAddIngredients = mutableListOf<Ingredient?>()
    lateinit var recipe: Recipe
    private var editMode = false
    private var editPosition: Int? = null
    private var alreadyPrepareAddList = false

    fun setEditMode(_editMode: Boolean, ingredient: Ingredient?) {
        viewModelScope.launch {
            ingredient?.keyDocument?.let {
                editPosition = listAddIngredients.getPositionIngredient(it)
            }
            editMode = _editMode
            _uiState.emit(UiState.EditMode(editMode))
        }
    }

    fun getEditMode(): Boolean = editMode

    private fun prepareAddLists() {
        if(!alreadyPrepareAddList) {
            recipe.ingredientList?.forEach { ingredient ->
                listAllIngredients.forEach { itemRecipe ->
                    itemRecipe?.let {
                        if (ingredient.keyDocument == it.keyDocument) {
                            listAddIngredients.add(
                                it.copy(volume = ingredient.volumeUsed)
                            )
                        }
                    }
                }
            }
            alreadyPrepareAddList = true
        }
    }

    fun getAllIngredients() {
        listIngredientUseCase()
            .catch { exception ->
                val error = exception.message
                _uiState.emit(UiState.Error(error))
            }
            .onEach {
                if (it.isNotEmpty()) {
                    listAllIngredients = it.convertToGOrMl()
                    prepareAddLists()
                    _uiState.emit(UiState.ListIngredient(listAllIngredients))
                } else {
                    _uiState.emit(UiState.DisableSpinner)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun List<Ingredient>.convertToGOrMl(): MutableList<Ingredient?> {
        return map { it.convertToUnit() }.toMutableList()
    }

    private fun Ingredient.convertToUnit(): Ingredient {
        return when (unit) {
            UnitMeasureType.KG.unit -> copy(
                unit = UnitMeasureType.G.unit,
                volume = volume?.times(1000)
            )

            UnitMeasureType.L.unit -> copy(
                unit = UnitMeasureType.ML.unit,
                volume = volume?.times(1000)
            )

            else -> this
        }
    }

    fun addIngredientInList(name: String?, volume: Float?) {
        viewModelScope.launch {
            if (editMode && name != null && volume != null) {
                editPosition?.let { position ->
                    listAddIngredients[position] =
                        listAddIngredients[position]?.copy(name = name, volume = volume)
                    editMode = false
                    recipe.ingredientList = listAddIngredients.toIngredientRecipeRegisterList()
                    _uiState.emit(UiState.SuccessEdit(position))
                }
            } else {
                val selectedIngredient = getSelectedIngredient(name)
                if (selectedIngredient != null && !listAddIngredients.containsIngredient(
                        selectedIngredient.keyDocument
                    )
                ) {
                    listAddIngredients.add(selectedIngredient.copy(volume = volume))
                    recipe.ingredientList = listAddIngredients.toIngredientRecipeRegisterList()
                    _uiState.emit(UiState.AddIngredient(selectedIngredient))
                } else {
                    _uiEvent.emit(UiEvent.ShowToast(DesignR.string.design_item_already_add))
                }
            }
        }
    }

    fun deleteItemAdd(ingredient: Ingredient?) {
        viewModelScope.launch {
            ingredient?.keyDocument?.let {
                val position = listAddIngredients.getPositionIngredient(it)
                listAddIngredients.removeAt(position)
                recipe.ingredientList = listAddIngredients.toIngredientRecipeRegisterList()
                _uiState.emit(UiState.RemoveIngredient(ingredient.id))
            }
        }
    }

    private suspend fun getSelectedIngredient(name: String?): Ingredient? {
        return listAllIngredients.firstOrNull { it?.name == name }
            ?: run {
                _uiState.emit(UiState.ErrorSpinner)
                null
            }
    }

    private fun MutableList<Ingredient?>.containsIngredient(keyDocument: String?): Boolean {
        return any { it?.keyDocument == keyDocument }
    }

    private fun MutableList<Ingredient?>.getPositionIngredient(keyDocument: String): Int {
        return indexOfFirst { it?.keyDocument == keyDocument }
    }
}