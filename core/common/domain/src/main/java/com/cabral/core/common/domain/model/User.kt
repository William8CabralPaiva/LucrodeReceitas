package com.cabral.core.common.domain.model

data class User(
    var email: String? = null,
    var name: String? = null,
    var password: String? = null,
    var key: String? = null
)

fun User.toUserRegister(): UserRegister {
    return UserRegister(name, email)
}
