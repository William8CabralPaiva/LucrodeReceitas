package com.cabral.core.common.domain.model

data class RecipeCosts(
    var name: String? = null,
    var volume: Float? = null,
    var profit: Float? = null,
    var profitPerUnit: Float? = null,
    var costs: Float? = null,
    var costsPerUnit: Float? = null,
    var totalProfit: Float? = null,
    var totalPerUnit: Float? = null,
    var ingredientList: MutableList<IngredientCosts>? = null,
    var keyDocument: String? = null,
)