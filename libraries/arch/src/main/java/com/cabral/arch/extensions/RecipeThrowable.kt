package com.cabral.arch.extensions

sealed class RecipeThrowable(cause: Throwable?) : Throwable(cause) {

    class AddRecipeThrowable(
        override val message: String = "Preencha o campo nome da Receita corretamente",
        throwable: Throwable? = null
    ) : RecipeThrowable(throwable)



}