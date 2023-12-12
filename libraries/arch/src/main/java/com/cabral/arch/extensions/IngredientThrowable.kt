package com.cabral.arch.extensions

sealed class IngredientThrowable(cause: Throwable?) : Throwable(cause) {

    class NameThrowable(
        override val message: String ="Preencha o campo Ingrediente",
        throwable: Throwable? = null
    ) : IngredientThrowable(throwable)

    class SizeThrowable(
        override val message: String ="Preencha o campo Quantidade Total",
        throwable: Throwable? = null
    ) : IngredientThrowable(throwable)

    class UnitThrowable(
        override val message: String ="Preencha o campo Unidade de Medida",
        throwable: Throwable? = null
    ) : IngredientThrowable(throwable)

    class PriceThrowable(
        override val message: String ="Preencha o campo Preço",
        throwable: Throwable? = null
    ) : IngredientThrowable(throwable)

}