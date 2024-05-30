package com.cabral.arch

import com.cabral.arch.extensions.UserThrowable

object PasswordUtils {
    fun validatePassword(password: String?): Boolean {
        val size = 8
        if (password != null) {
            val hasUpperCase = password.any { it.isUpperCase() }
            val hasLowerCase = password.any { it.isLowerCase() }
            val hasNumber = password.any { it.isDigit() }
            val hasEspecialCharacter = password.any { it.isLetterOrDigit().not() }

            if (password.length < size) {
                throw UserThrowable.AuthenticatePasswordThrowable("A senha deve ter pelo menos $size caracteres.")
            }

            if (!hasUpperCase) {
                throw UserThrowable.AuthenticatePasswordThrowable("A senha deve conter pelo menos uma letra maiúscula.")
            }

            if (!hasLowerCase) {
                throw UserThrowable.AuthenticatePasswordThrowable("A senha deve conter pelo menos uma letra minúscula.")
            }

            if (!hasNumber) {
                throw UserThrowable.AuthenticatePasswordThrowable("A senha deve conter pelo menos um número.")
            }

            if (!hasEspecialCharacter) {
                throw UserThrowable.AuthenticatePasswordThrowable("A senha deve conter pelo menos um caractere especial.")
            }
        } else {
            throw UserThrowable.AuthenticatePasswordThrowable("A senha deve ter pelo menos $size caracteres.")
        }

        return true

    }

    fun validatePasswordLogin(password: String?): Boolean {
        password?.let {
            if (password.isNotEmpty()) {
                return true
            }
        }
        throw UserThrowable.AuthenticatePasswordThrowable("Preencha o campo senha corretamente")
    }
}