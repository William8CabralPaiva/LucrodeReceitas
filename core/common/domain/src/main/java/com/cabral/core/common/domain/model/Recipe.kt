package com.cabral.core.common.domain.model

data class Recipe(
    var id: Int,
    var name: String? = null,
    var price: Float? = null,
    var keyDocument: String? = null
)
