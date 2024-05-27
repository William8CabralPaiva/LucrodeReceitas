package com.cabral.core.common.domain.usecase

import com.cabral.core.common.domain.model.Ingredient
import com.cabral.core.common.domain.model.IngredientCosts
import com.cabral.core.common.domain.model.Recipe
import com.cabral.core.common.domain.model.RecipeCosts
import com.cabral.core.common.domain.model.UnitMeasureType
import com.cabral.core.common.domain.repository.IngredientRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

class CostRecipeUseCase(private val repository: IngredientRepository) {
    operator fun invoke(recipe: Recipe): Flow<RecipeCosts> = flow {

        var totalRecipe = 0f
        val recipeCosts = RecipeCosts()
        recipeCosts.ingredientList = mutableListOf()

        val result = repository.getAllIngredients()

        result.catch {
            throw Throwable()
        }

        result.collect { costs ->
            recipe.ingredientList?.forEach {
                costs.forEach { itemCost ->
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
        }

        recipeCosts.run {
            name = recipe.name
            total = totalRecipe
            recipe.expectedProfit?.let {
                if (recipe.expectedProfit != 0f) {
                    val profit = totalRecipe * it / 100
                    profitPrice = totalRecipe + profit
                }
            }
        }
        emit(recipeCosts)

    }

    fun Ingredient.convertToUnit(): Ingredient {
        when (unit) {
            UnitMeasureType.KG.unit -> {
                unit = UnitMeasureType.G.unit
                volume = volume?.times(1000)
            }

            UnitMeasureType.L.unit -> {
                unit = UnitMeasureType.ML.unit
                volume = volume?.times(1000)
            }
        }
        return this
    }
}