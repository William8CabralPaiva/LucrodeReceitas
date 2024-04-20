package com.cabral.core.common.domain.model

data class Recipe(
    var id: Int,
    var name: String? = null,
    var volumeUnit: Float? = null,
    var expectedProfit: Float? = null,
    var ingredientList: MutableList<Ingredient>? = null,
    var keyDocument: String? = null
)

fun Recipe.toRecipeRegister(key: String?): RecipeRegister {
    val list = mutableListOf<IngredientRecipeRegister>()

    ingredientList?.forEach {
        list.add(it.toIngredientRecipeRegister())
    }

    val keyId = if (keyDocument == null) {
        key
    } else {
        keyDocument
    }

    return RecipeRegister(name, volumeUnit, expectedProfit, list, keyId)
}
