package com.cabral.core

import android.app.Activity
import android.content.Intent
import com.cabral.core.presentation.LoggedActivity
import com.cabral.core.presentation.NotLoggedActivity

class NavigationUtils {

    companion object {
        fun splashToLogin(activity: Activity) {
            activity.finish()
            val intent = Intent(activity, NotLoggedActivity::class.java)
            activity.startActivity(intent)
        }

        fun loginToRecipe(activity: Activity) {
            activity.finish()
            val intent = Intent(activity, LoggedActivity::class.java)
            activity.startActivity(intent)
        }
    }
}