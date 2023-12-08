package com.cabral.arch

import com.cabral.arch.extensions.RecipeThrowable

object PasswordUtils {
    fun validatePassword(password: String): Boolean {
        val size = 8
        val hasUpperCase = password.any { it.isUpperCase() }
        val hasLowerCase = password.any { it.isLowerCase() }
        val hasNumber = password.any { it.isDigit() }
        val hasEspecialCharacter = password.any { it.isLetterOrDigit().not() }

        if (password.length < size) {
            throw RecipeThrowable.AuthenticatePassword("A senha deve ter pelo menos $size caracteres.")
        }

        if (!hasUpperCase) {
            throw RecipeThrowable.AuthenticatePassword("A senha deve conter pelo menos uma letra maiúscula.")
        }

        if (!hasLowerCase) {
            throw RecipeThrowable.AuthenticatePassword("A senha deve conter pelo menos uma letra minúscula.")
        }

        if (!hasNumber) {
            throw RecipeThrowable.AuthenticatePassword("A senha deve conter pelo menos um número.")
        }

        if (!hasEspecialCharacter) {
            throw RecipeThrowable.AuthenticatePassword("A senha deve conter pelo menos um caractere especial.")
        }

        return true

    }
}