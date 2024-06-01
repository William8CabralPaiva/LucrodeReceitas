package com.cabral.core.common.domain.model

data class RecipeProfitPrice(
    var id: Int,
    var name: String? = null,
    var volume: Float? = null,
    var expectedProfit: Float? = null,
    var profitPrice: Float? = null,
    var profitPriceUnit: Float? = null,
    var costs: Float? = null,
    var costsPerUnit: Float? = null,
    var ingredientList: MutableList<IngredientRecipeRegister>,
    var keyDocument: String? = null
)