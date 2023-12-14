package com.cabral.arch.extensions

sealed class GenericThrowable(cause: Throwable?) : Throwable(cause) {

    class FailThrowable(
        override val message: String = "Houve um problema verifique sua conexão",
        throwable: Throwable? = null
    ) : GenericThrowable(throwable)
}