package com.cabral.core.common.domain.model


data class IngredientCosts(
    //todo converter number
    var name: String? = null,
    var volume: Float? = null,
    var unit: String? = null,
    var priceUnit: Float? = null,
    var total:Float? = null,
    var keyDocument: String? = null,
)