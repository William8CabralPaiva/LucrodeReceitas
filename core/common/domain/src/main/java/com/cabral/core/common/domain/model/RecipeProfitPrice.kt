package com.cabral.core.common.domain.model

data class RecipeProfitPrice(
    var id: Int,
    var name: String? = null,
    var volumeUnit: Float? = null,
    var expectedProfit: Float? = null,
    var profitPrice: Float? = null,
    var ingredientList: MutableList<IngredientRecipeRegister>? = null,
    var keyDocument: String? = null
)