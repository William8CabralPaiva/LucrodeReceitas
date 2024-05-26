package com.cabral.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.IngredientRegister
import com.cabral.core.common.domain.model.getUnitMeasureByUnit
import kotlinx.parcelize.Parcelize


@Keep
@Parcelize
data class IngredientArgs(
    var id: Int,
    var name: String? = null,
    var volume: Float? = null,
    var unit: String? = null,
    var price: Float? = null,
    var keyDocument: String? = null
) : Parcelable

fun IngredientArgs.toIngredientRegister(): IngredientRegister {
    val enum = getUnitMeasureByUnit(unit)
    return IngredientRegister(name, volume, enum.type, price, keyDocument)
}

fun IngredientArgs.toIngredient(): Ingredient {
    return Ingredient(id, name, volume, unit, price, keyDocument)
}

fun Ingredient.toIngredientArgs(): IngredientArgs {
    return IngredientArgs(id, name, volume, unit, price, keyDocument)
}

fun MutableList<IngredientArgs>.toListIngredient(): MutableList<Ingredient>? {
    val list = mutableListOf<Ingredient>()
    forEach {
        val ingredient = Ingredient(it.id, it.name, it.volume, it.unit, it.price, it.keyDocument)
        list.add(ingredient)
    }
    return list
}

fun MutableList<Ingredient>.toListIngredientArgs(): MutableList<IngredientArgs>? {
    val list = mutableListOf<IngredientArgs>()
    forEach {
        val ingredient =
            IngredientArgs(it.id, it.name, it.volume, it.unit, it.price, it.keyDocument)
        list.add(ingredient)
    }
    return list
}

