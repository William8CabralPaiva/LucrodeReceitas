package com.cabral.recipe.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.IngredientRecipeRegister
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.core.common.domain.model.toIngredientRecipeRegisterList
import com.cabral.core.common.domain.usecase.ListIngredientUseCase
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

    var listAllIngredients = mutableListOf<Ingredient?>()

    var listAddIngredients = mutableListOf<Ingredient?>()

    lateinit var recipe: Recipe

    private var editMode = false

    var editPosition: Int? = null

    fun setEditMode(_editMode: Boolean, ingredient: Ingredient?) {
        ingredient?.keyDocument?.let {
            recipe.ingredientList?.let { ingredientList ->
                editPosition = ingredientList.getPosition(it)
            }
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

    fun prepareLists() {
        listAddIngredients = mutableListOf()
        recipe.ingredientList?.forEach { ingredient ->
            listAllIngredients.forEach { itemRecipe ->
                itemRecipe?.let {
                    if (ingredient.keyDocument == itemRecipe.keyDocument) {
                        val item = Ingredient(
                            itemRecipe.id,
                            itemRecipe.name,
                            itemRecipe.volume,
                            itemRecipe.unit,
                            itemRecipe.price,
                            ingredient.keyDocument,
                            ingredient.volumeUsed
                        )
                        listAddIngredients.add(item)
                    }
                }
            }
        }
    }

    fun getAllIngredients() {
        listIngredientUseCase()
            .onStart { _notifyStartLoading.postValue(Unit) }
            .catch {
                _notifyEmptyList.postValue(Unit)
            }.onEach {
                if (it.isNotEmpty()) {
                    listAllIngredients = it.convertToGOrMl()
                    prepareLists().also {
                        _notifyListIngredient.postValue(listAllIngredients)
                    }
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
            listAllIngredients.let { ingredientList ->
                editPosition?.let {
                    ingredientList[it].run {
                        this?.name = name
                        this?.volume = volume
                    }
                    editMode = false
                    _notifyEditMode.postValue(false)
                    _notifySuccessEdit.postValue(ingredientList[it]?.id)
                }
            }
        } else {
            listAddIngredients.let { ingredientList ->
                val selectedIngredient =
                    getSelectedIngredient(name)
                if (selectedIngredient != null &&
                    !ingredientList.contains(selectedIngredient)
                ) {
                    ingredientList.add(selectedIngredient)
                    recipe.ingredientList = listAddIngredients.toIngredientRecipeRegisterList()
                    _notifyAddIngredient.postValue(selectedIngredient)
                } else {
                    _notifyShowToast.postValue(RecipeR.string.recipe_item_already_add)
                }
            }
        }
    }

    fun deleteItemAdd(ingredient: Ingredient?) {
        recipe.ingredientList?.let { ingredientList ->
            ingredient?.keyDocument?.let {
                val position = ingredientList.getPosition(it)
                ingredientList.remove(ingredientList[position])
                _notifyRemoveIngredient.postValue(ingredient.id)
            }
        }
    }

    private fun getSelectedIngredient(name: String?): Ingredient? {
        return try {
            listAllIngredients.first { ingredient ->
                ingredient?.name == name
            }
        } catch (_: Exception) {
            null
        }
    }

    private fun MutableList<IngredientRecipeRegister>.getPosition(keyDocument: String): Int {
        return indexOfFirst { it.keyDocument == keyDocument }
    }
}
