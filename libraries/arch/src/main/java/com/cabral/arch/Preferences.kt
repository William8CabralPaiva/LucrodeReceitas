package com.cabral.arch

import android.content.Context

const val ENCRYPT = "fXHXjBrZd0iGtPLe"
const val USER = "USER"
const val PREFS_FILENAME = "com.cabral.lucrodereceitas"

fun Context.saveUserKey(value: String) {
    encrypt(USER, value)
}

fun Context.getUserKey(): String? {
    return decrypt(USER)
}

private fun Context.encrypt(key: String, value: String) {
    val sharedPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    val cryptoValue = CryptoUtils.encrypt(ENCRYPT, value)
    sharedPreferences.edit().putString(key, cryptoValue).apply()
}

private fun Context.decrypt(key: String): String? {
    try {
        val sharedPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(key, "")
        if (!value.isNullOrEmpty()) {
            val decrypt = CryptoUtils.decrypt(ENCRYPT, value)
            return decrypt
        }
        return value
    } catch (_: Exception) {
        return ""
    }
}


