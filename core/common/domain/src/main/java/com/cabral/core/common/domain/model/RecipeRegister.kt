package com.cabral.core.common.domain.model

data class RecipeRegister(
    var name: String? = null,
    var volumeUnit: Float? = null,
    var expectedProfit: Float? = null,
    var ingredientList: List<IngredientRecipeRegister>? = null,
    var keyDocument: String? = null
)

fun RecipeRegister.toRecipe(id: Int): Recipe {
    //todo ingredientList tirar nulo
    return Recipe(id, name, volumeUnit, expectedProfit, null, keyDocument)
}
