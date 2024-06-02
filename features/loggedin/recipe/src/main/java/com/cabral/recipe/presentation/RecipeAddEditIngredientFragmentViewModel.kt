package com.cabral.recipe.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.arch.Event
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.IngredientRecipeRegister
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.core.common.domain.model.toIngredientRecipeRegister
import com.cabral.core.common.domain.model.toIngredientRecipeRegisterList
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import com.cabral.design.R as DesignR

class RecipeAddEditIngredientFragmentViewModel(
    private val listIngredientUseCase: ListIngredientUseCase,
) : ViewModel() {

    private val _notifyListIngredient = MutableLiveData<Event<List<Ingredient?>>>()
    val notifyListIngredient: LiveData<Event<List<Ingredient?>>> = _notifyListIngredient

    private val _notifyRemoveIngredient = MutableLiveData<Event<Int>>()
    val notifyRemoveIngredient: LiveData<Event<Int>> = _notifyRemoveIngredient

    private val _notifySuccessEdit = MutableLiveData<Event<Int>>()
    val notifySuccessEdit: LiveData<Event<Int>> = _notifySuccessEdit

    private val _notifyError = MutableLiveData<Event<String>>()
    val notifyError: LiveData<Event<String>> = _notifyError

    private val _notifyErrorSpinner = MutableLiveData<Event<Unit>>()
    val notifyErrorSpinner: LiveData<Event<Unit>> = _notifyErrorSpinner

    private val _notifyShowToast = MutableLiveData<Event<Int>>()
    val notifyShowToast: LiveData<Event<Int>> = _notifyShowToast

    private val _notifyAddIngredient = MutableLiveData<Event<Ingredient?>>()
    val notifyAddIngredient: LiveData<Event<Ingredient?>> = _notifyAddIngredient

    private val _notifyEditMode = MutableLiveData<Event<Boolean>>()
    val notifyEditMode: LiveData<Event<Boolean>> = _notifyEditMode

    var listAllIngredients = mutableListOf<Ingredient?>()

    var listAddIngredients = mutableListOf<Ingredient?>()

    lateinit var recipe: Recipe

    private var editMode = false

    private var editPosition: Int? = null

    fun setEditMode(_editMode: Boolean, ingredient: Ingredient?) {
        ingredient?.keyDocument?.let {
            listAddIngredients.let { ingredientList ->
                editPosition = ingredientList.getPositionIngredient(it)
            }
        }
        editMode = _editMode
        _notifyEditMode.postValue(Event(editMode))
    }

    fun getEditMode(): Boolean {
        return editMode
    }


    private fun prepareLists() {
        listAddIngredients = mutableListOf()
        recipe.ingredientList?.forEach { ingredient ->
            listAllIngredients.forEach { itemRecipe ->
                itemRecipe?.let {
                    if (ingredient.keyDocument == itemRecipe.keyDocument) {
                        val item = Ingredient(
                            itemRecipe.id,
                            itemRecipe.name,
                            ingredient.volumeUsed,
                            itemRecipe.unit,
                            itemRecipe.price,
                            ingredient.keyDocument,
                        )
                        listAddIngredients.add(item)
                    }
                }
            }
        }
    }

    fun getAllIngredients() {
        listIngredientUseCase()
            .catch {
                _notifyError.postValue(Event(it.message))
            }.onEach {
                if (it.isNotEmpty()) {
                    listAllIngredients = it.convertToGOrMl()
                    prepareLists().also {
                        _notifyListIngredient.postValue(Event(listAllIngredients))
                    }
                }
            }
            .launchIn(viewModelScope)
    }

    private fun List<Ingredient>.convertToGOrMl(): MutableList<Ingredient?> {
        val list = mutableListOf<Ingredient?>()
        forEach {
            val ingredient = it.convertToUnit()
            list.add(ingredient)
        }
        return list
    }

    private fun Ingredient.convertToUnit(): Ingredient {
        when (unit) {
            UnitMeasureType.KG.unit -> {
                unit = UnitMeasureType.G.unit
                volume = volume?.times(1000)
            }

            UnitMeasureType.L.unit -> {
                unit = UnitMeasureType.ML.unit
                volume = volume?.times(1000)
            }
        }
        return this
    }

    fun addIngredientInList(name: String?, volume: Float?) {
        if (editMode && name != null && volume != null) {
            listAddIngredients.let { ingredientList ->
                editPosition?.let {
                    ingredientList[it].run {
                        this?.name = name
                        this?.volume = volume
                    }
                    editMode = false
                    recipe.ingredientList = listAddIngredients.toIngredientRecipeRegisterList()
                    _notifyEditMode.postValue(Event(false))

                    val position =
                        recipe.ingredientList?.indexOf(ingredientList[it]?.toIngredientRecipeRegister())
                    position?.let {
                        _notifySuccessEdit.postValue(Event(it))
                    }
                }
            }
        } else {
            listAddIngredients.let { ingredientList ->
                val selectedIngredient =
                    getSelectedIngredient(name)
                if (selectedIngredient != null &&
                    !ingredientList.containsIngredient(selectedIngredient.keyDocument)
                ) {

                    ingredientList.add(selectedIngredient)
                    selectedIngredient.volume = volume
                    recipe.ingredientList = listAddIngredients.toIngredientRecipeRegisterList()
                    _notifyAddIngredient.postValue(Event(selectedIngredient))
                } else {
                    _notifyShowToast.postValue(Event(DesignR.string.design_item_already_add))
                }
            }
        }
    }

    fun deleteItemAdd(ingredient: Ingredient?) {
        listAddIngredients.let { ingredientList ->
            ingredient?.keyDocument?.let {
                val position = ingredientList.getPositionIngredient(it)
                ingredientList.remove(ingredientList[position])
                recipe.ingredientList = listAddIngredients.toIngredientRecipeRegisterList()
                _notifyRemoveIngredient.postValue(Event(ingredient.id))
            }
        }
    }

    private fun getSelectedIngredient(name: String?): Ingredient? {
        return try {
            listAllIngredients.first { ingredient ->
                ingredient?.name == name
            }
        } catch (_: Exception) {
            _notifyErrorSpinner.postValue(Event(Unit))
            null
        }
    }

    private fun MutableList<Ingredient?>.containsIngredient(keyDocument: String?): Boolean {
        val list = filter { it?.keyDocument == keyDocument }
        return list.isNotEmpty()
    }

    private fun MutableList<Ingredient?>.getPositionIngredient(keyDocument: String): Int {
        return indexOfFirst { it?.keyDocument == keyDocument }
    }

    private fun MutableList<IngredientRecipeRegister>.getPosition(keyDocument: String): Int {
        return indexOfFirst { it.keyDocument == keyDocument }
    }
}
