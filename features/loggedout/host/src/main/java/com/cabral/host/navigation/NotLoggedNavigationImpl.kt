package com.cabral.host.navigation

import android.app.Activity
import android.content.Intent
import com.cabral.core.NotLoggedNavigation
import com.cabral.host.presentation.NotLoggedActivity

internal class NotLoggedNavigationImpl : NotLoggedNavigation {
    override fun openNotLogged(activity: Activity) {
        activity.finish()
        val intent = Intent(activity, NotLoggedActivity::class.java)
        activity.startActivity(intent)
    }
}