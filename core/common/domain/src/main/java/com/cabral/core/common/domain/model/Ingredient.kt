package com.cabral.core.common.domain.model

data class Ingredient(
    val id: Int,
    val name: String,
    val volume: String,
    val unit: String,
    val price: Float
)