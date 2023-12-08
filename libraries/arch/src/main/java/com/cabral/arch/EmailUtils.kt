package com.cabral.arch

import com.cabral.arch.extensions.RecipeThrowable

object EmailUtils {
    fun validateEmail(email: String): Boolean {
        val regex = Regex("^\\S+@\\S+\\.\\S+$")
        val result = regex.matches(email)
        if (!result) {
            throw RecipeThrowable.AuthenticateEmail("Email inválido")
        }
        return true
    }
}