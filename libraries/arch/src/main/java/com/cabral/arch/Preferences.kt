package com.cabral.arch

import android.content.Context

const val USERKEY = "USERKEY12345678"
const val USER = "USER"
const val PREFS_FILENAME = "com.cabral.lucrodereceitas"

fun Context.saveUserKey(key: String) {
    val sharedPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    val cryptoValue = CryptoUtils.encrypt(USERKEY, key)
    sharedPreferences.edit().putString(USER, cryptoValue).apply()
}

fun Context.getUserKey(): String? {
    try {
        val sharedPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(USER, "")
        if (!value.isNullOrEmpty()) {
            val decrypt = CryptoUtils.decrypt(USERKEY, value)
            return decrypt
        }
        return value
    } catch (_: Exception) {
        return ""
    }
}


