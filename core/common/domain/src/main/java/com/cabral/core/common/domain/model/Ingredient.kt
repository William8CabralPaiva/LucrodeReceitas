package com.cabral.core.common.domain.model


data class Ingredient(
    var id: Int,
    var name: String? = null,
    var volume: Float? = null,
    var unit: String? = null,
    var price: Float? = null,
    var keyDocument: String? = null,
)

fun Ingredient.toIngredientRegister(key: String?): IngredientRegister {
    val enum = getUnitMeasureByUnit(unit)

    val keyId = if (keyDocument == null) {
        key
    } else {
        keyDocument
    }

    return IngredientRegister(name, volume, enum.type, price, keyId)
}

fun MutableList<Ingredient?>.toIngredientRecipeRegisterList(): MutableList<IngredientRecipeRegister> {
    val list = mutableListOf<IngredientRecipeRegister>()
    forEach {
        it?.run {
            list.add(toIngredientRecipeRegister())
        }
    }
    return list
}

fun Ingredient.toIngredientRecipeRegister(): IngredientRecipeRegister {
    return IngredientRecipeRegister(keyDocument, volume)
}

fun Ingredient.convertToUnit(): Ingredient {
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