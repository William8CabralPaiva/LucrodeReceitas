package com.cabral.arch

import com.cabral.arch.extensions.UserThrowable

object EmailUtils {
    fun validateEmail(email: String?): Boolean {
        if (email != null) {
            val regex = Regex("^\\S+@\\S+\\.\\S+$")
            val result = regex.matches(email)
            if (!result) {
                throw UserThrowable.AuthenticateEmailThrowable()
            }
        } else {
            throw UserThrowable.AuthenticateEmailThrowable("Preencha o campo email")
        }
        return true
    }
}