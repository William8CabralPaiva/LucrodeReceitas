package com.cabral.core.common.domain.model

data class RecipeCosts(
    var name: String? = null,
    var profitPrice: Float? = null,
    var price: Float? = null,
    var total: Float? = null,
    var ingredientList: MutableList<IngredientCosts>? = null,
    var keyDocument: String? = null,
)