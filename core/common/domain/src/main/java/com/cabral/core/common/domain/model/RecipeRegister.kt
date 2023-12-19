package com.cabral.core.common.domain.model

data class RecipeRegister(
    var name: String? = null,
    var price: Float? = null,
    var keyDocument: String? = null
)

fun RecipeRegister.toRecipe(id: Int): Recipe {
    return Recipe(id, name, price, keyDocument)
}
