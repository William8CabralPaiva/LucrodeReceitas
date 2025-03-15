package com.cabral.test_utils.stubs

import com.cabral.core.common.domain.model.Ingredient

fun ingredientStub(): Ingredient {
    return Ingredient(id = 1, name = "Arroz", volume = 1000.0f, unit = "g", price = 2.0f)
}

fun ingredientListStub(): List<Ingredient> {
    return listOf(
        ingredientStub(),
        ingredientStub()
    )
}