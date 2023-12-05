package com.cabral.arch.extensions



sealed class RecipeThrowable(cause: Throwable?) : Throwable(cause) {

    class AddUserThrowable(
        override val message: String? = "Usuário ja cadastrado",
        throwable: Throwable? = null
    ) : RecipeThrowable(throwable)

    class AddUserThrowableError(
        override val message: String? = "Erro ao cadastrar usuário",
        throwable: Throwable? = null
    ) : RecipeThrowable(throwable)

    data class UnknownThrowable(
        override val message: String?,
        val throwable: Throwable? = null
    ) : RecipeThrowable(throwable)
}