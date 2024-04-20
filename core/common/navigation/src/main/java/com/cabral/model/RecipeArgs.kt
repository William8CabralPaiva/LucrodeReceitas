package com.cabral.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import androidx.annotation.Keep
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.Recipe

@Keep
@Parcelize
data class RecipeArgs(
    var id: Int,
    var name: String? = null,
    var volumeUnit: Float? = null,
    var expectedProfit: Float? = null,
    var ingredientList: MutableList<IngredientArgs>? = null,
    var keyDocument: String? = null
) : Parcelable

fun RecipeArgs.toRecipe(): Recipe {
    val listIngredient = ingredientList?.toListIngredient()
    return Recipe(id, name, volumeUnit, expectedProfit, listIngredient, keyDocument)
}

fun Recipe.toRecipeArgs(): RecipeArgs {
    val listIngredient = ingredientList?.toListIngredientArgs()
    return RecipeArgs(id, name, volumeUnit, expectedProfit, listIngredient, keyDocument)
}