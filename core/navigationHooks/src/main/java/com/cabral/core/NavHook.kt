package com.cabral.core

import android.app.Activity

object NavHook {

    fun splashToLogin(activity: Activity) {
        NavigationUtils.loginToRecipe(activity)
    }

    fun loginToRecipe(activity: Activity) {
        NavigationUtils.loginToRecipe(activity)
    }
}