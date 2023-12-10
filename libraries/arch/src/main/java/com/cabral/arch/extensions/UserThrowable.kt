package com.cabral.arch.extensions


sealed class UserThrowable(cause: Throwable?) : Throwable(cause) {

    class AddUserThrowable(
        override val message: String? = "Usuário já cadastrado",
        throwable: Throwable? = null
    ) : UserThrowable(throwable)

    class AddUserErrorThrowable(
        override val message: String? = "Erro ao cadastrar usuário",
        throwable: Throwable? = null
    ) : UserThrowable(throwable)


    data class UnknownUserThrowable(
        override val message: String? = "Usuário não encontrado",
        val throwable: Throwable? = null
    ) : UserThrowable(throwable)

    data class AuthenticateEmailThrowable(
        override val message: String = "Email inválido",
        val throwable: Throwable? = null
    ) : UserThrowable(throwable)

    data class AuthenticatePasswordThrowable(
        override val message: String,
        val throwable: Throwable? = null
    ) : UserThrowable(throwable)

    data class UserAlreadyRegisterPasswordThrowable(
        override val message: String = "Usuário ja cadastrado",
        val throwable: Throwable? = null
    ) : UserThrowable(throwable)

    data class UsernameRegisterThrowable(
        override val message: String = "Nome de usuário inválido",
        val throwable: Throwable? = null
    ) : UserThrowable(throwable)

    data class NotEqualPasswordThrowable(
        override val message: String = "As senhas devem ser iguais",
        val throwable: Throwable? = null
    ) : UserThrowable(throwable)


}