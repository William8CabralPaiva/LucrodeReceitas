package com.cabral.core.common.domain.model

data class Ingredient(
    var id: Int,
    var name: String? = null,
    var volume: Float? = null,
    var unit: String? = null,
    var price: Float? = null,
    var keyDocument: String? = null
)

fun Ingredient.toIngredientRegister(): IngredientRegister {
    val enum = getUnitMeasureByUnit(unit)
    return IngredientRegister(name, volume, enum.type, price, keyDocument)
}