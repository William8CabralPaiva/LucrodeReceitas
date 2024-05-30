package com.cabral.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class RecipeArgs(
    var id: Int,
    var name: String? = null,
    var volumeUnit: Float? = null,
    var expectedProfit: Float? = null,
    var ingredientList: MutableList<IngredientRecipeRegisterArgs>? = null,
    var keyDocument: String? = null
) : Parcelable

fun RecipeArgs.toRecipe(): Recipe {
    val listIngredient = ingredientList?.toIngredientRecipeRegister()
    return Recipe(id, name, volumeUnit, expectedProfit, listIngredient, keyDocument)
}

fun Recipe.toRecipeArgs(): RecipeArgs {
    val listIngredient = ingredientList
    return RecipeArgs(
        id,
        name,
        volumeUnit,
        expectedProfit,
        listIngredient?.toIngredientRecipeRegisterArgs(),
        keyDocument
    )
}

fun RecipeProfitPrice.toRecipeArgs(): RecipeArgs {
    val listIngredient = ingredientList
    return RecipeArgs(
        id,
        name,
        volumeUnit,
        expectedProfit,
        listIngredient?.toIngredientRecipeRegisterArgs(),
        keyDocument
    )
}

