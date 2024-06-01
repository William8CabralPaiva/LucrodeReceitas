package com.cabral.core.common

import com.cabral.core.common.domain.model.User

class SingletonUser private constructor() {

    private var user: User? = null

    fun reset() {
        user = null
    }

    fun getUser(): User? {
        return user
    }

    fun setUser(_user: User) {
        user = _user
    }

    fun getKey(): String? {
        try {
            return user?.key
        } catch (_: Exception) {

        }
        return null
    }

    companion object {
        private var singletonUser = SingletonUser()

        @JvmStatic
        fun getInstance(): SingletonUser {
            return singletonUser
        }
    }
}