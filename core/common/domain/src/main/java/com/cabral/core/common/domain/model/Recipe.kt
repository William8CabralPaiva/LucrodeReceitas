package com.cabral.core.common.domain.model

data class Recipe(
    var id: Int,
    var name: String? = null,
    var volumeUnit: Float? = null,
    var expectedProfit: Float? = null,
    var ingredientList: MutableList<IngredientRecipeRegister>? = null,
    var keyDocument: String? = null
)

fun Recipe.toRecipeRegister(key: String?): RecipeRegister {

    val keyId = if (keyDocument == null) {
        key
    } else {
        keyDocument
    }

    return RecipeRegister(name, volumeUnit, expectedProfit, ingredientList, keyId)
}


fun Recipe.toRecipeCosts(costs: List<Ingredient>): RecipeCosts {
    var totalRecipe = 0f
    val recipeCosts = RecipeCosts()
    recipeCosts.ingredientList = mutableListOf()

    costs.forEach { itemCost ->
        ingredientList?.forEach {
            if (it.keyDocument == itemCost.keyDocument) {
                val ingredientCosts = IngredientCosts()

                val ingredient = itemCost.convertToUnit()

                ingredientCosts.run {
                    keyDocument = itemCost.keyDocument
                    name = itemCost.name
                    unit = ingredient.unit
                    volume = it.volumeUsed
                }

                ingredient.volume?.let { volume ->
                    ingredient.price?.let { price ->
                        if (volume != 0f && price != 0f) {
                            val priceUnit = price / volume
                            ingredientCosts.priceUnit = priceUnit

                            ingredientCosts.volume?.run {
                                val total = priceUnit * this
                                ingredientCosts.total = total
                                totalRecipe += total
                            }

                        }
                    }
                }
                recipeCosts.ingredientList?.add(ingredientCosts)
            }
        }

    }

    recipeCosts.let {
        it.name = name
        it.total = totalRecipe
        expectedProfit?.let { expectedProfit ->
            if (expectedProfit != 0f) {
                val profit = totalRecipe * expectedProfit / 100
                it.profitPrice = totalRecipe + profit
            }
        }
    }

    return recipeCosts
}


fun Recipe.toRecipeProfitPrice(list: List<Ingredient>): RecipeProfitPrice {
    var totalRecipe = 0f


    list.forEach { itemCost ->
        ingredientList?.forEach {
            if (it.keyDocument == itemCost.keyDocument) {

                val ingredientCosts = IngredientCosts()

                val ingredient = itemCost.convertToUnit()

                ingredientCosts.run {
                    keyDocument = itemCost.keyDocument
                    name = itemCost.name
                    unit = ingredient.unit
                    volume = it.volumeUsed
                }

                ingredient.volume?.let { volume ->
                    ingredient.price?.let { price ->
                        if (volume != 0f && price != 0f) {
                            val priceUnit = price / volume
                            ingredientCosts.priceUnit = priceUnit

                            ingredientCosts.volume?.run {
                                val total = priceUnit * this
                                ingredientCosts.total = total
                                totalRecipe += total
                            }

                        }
                    }
                }
            }
        }

    }

    val recipeProfitPrice = RecipeProfitPrice(
       id,
        name,
        volumeUnit,
        expectedProfit,
        null,
        ingredientList,
        keyDocument
    )

    recipeProfitPrice.let {
        expectedProfit?.let { expectedProfit ->
            if (expectedProfit != 0f) {
                val profit = totalRecipe * expectedProfit / 100
                it.profitPrice = totalRecipe + profit
            }
        }
    }

    return recipeProfitPrice
}

fun List<Recipe>.toListRecipeProfitPrice(ingredientList: List<Ingredient>): List<RecipeProfitPrice> {
    val list = mutableListOf<RecipeProfitPrice>()
    forEach {
        val item = it.toRecipeProfitPrice(ingredientList)
        list.add(item)
    }
    return list
}