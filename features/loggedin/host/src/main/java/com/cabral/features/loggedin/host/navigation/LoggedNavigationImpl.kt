package com.cabral.features.loggedin.host.navigation

import android.app.Activity
import android.content.Intent
import com.cabral.core.LoggedNavigation
import com.cabral.features.loggedin.host.presentation.LoggedActivity


internal class LoggedNavigationImpl : LoggedNavigation {

    override fun openActivityLogged(activity: Activity) {
        activity.finish()
        val intent = Intent(activity, LoggedActivity::class.java)
        activity.startActivity(intent)
    }
}