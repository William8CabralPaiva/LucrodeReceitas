package com.cabral.stubs

import com.cabral.core.common.domain.model.IngredientRecipeRegister
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeProfitPrice

fun recipeProfitPriceStub(): RecipeProfitPrice {
    return RecipeProfitPrice(
        id = 1,
        name = "Arroz",
        volume = 1000.0f,
        expectedProfit = 0.5f,
        profitPrice = 2.0f,
        profitPriceUnit = 2.0f,
        costs = 1.0f,
        costsPerUnit = 1.0f,
        ingredientList = mutableListOf(
            IngredientRecipeRegister("Arroz", 1.0f)
        ),
        keyDocument = ""
    )
}

fun listRecipeProfitPriceStub(): List<RecipeProfitPrice> {
    return listOf(
        recipeProfitPriceStub(),
        recipeProfitPriceStub()
    )
}

fun recipeStub(): Recipe {
    return Recipe(id = 1, name = "Fermento", volume = 10f, expectedProfit = 10f)
}