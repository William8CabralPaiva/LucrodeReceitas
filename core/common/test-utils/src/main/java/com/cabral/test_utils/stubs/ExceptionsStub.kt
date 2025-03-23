package com.cabral.test_utils.stubs

import java.lang.Exception

fun stubNetWorkException(): Exception {
    return NetworkException(
        "Falha na internet",
        Throwable("Houve um problema verifique sua conexão")
    )
}

open class NetworkException(message: String?, cause: Throwable?) : Exception(message, cause)