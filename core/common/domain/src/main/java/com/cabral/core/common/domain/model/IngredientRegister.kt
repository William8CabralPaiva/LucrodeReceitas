package com.cabral.core.common.domain.model

data class IngredientRegister(
    var name: String? = null,
    var volume: Float? = null,
    var unit: String? = null,
    var price: Float? = null,
    var keyDocument: String? = null
)

fun IngredientRegister.toIngredient(id: Int): Ingredient {
    val enum = getUnitMeasureByType(unit)
    return Ingredient(id, name, volume, enum.unit, price, keyDocument)
}