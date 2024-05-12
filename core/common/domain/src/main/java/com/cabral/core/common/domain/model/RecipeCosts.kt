package com.cabral.core.common.domain.model

data class RecipeCosts(
    //todo trocar os floats pra number
    var name: String? = null,
    var expectedProfit: Float? = null,
    var price: Float? = null,
    var total: Float? = null,
    var ingredientList: MutableList<IngredientCosts>? = null,
    var keyDocument: String? = null,
)