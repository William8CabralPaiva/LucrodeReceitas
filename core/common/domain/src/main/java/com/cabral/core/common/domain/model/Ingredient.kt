package com.cabral.core.common.domain.model

data class Ingredient(
    val id: Int,
    val name: String?,
    val volume: Float?,
    val unit: String?,
    val price: Float?,
    val keyDocument: String? = null
)