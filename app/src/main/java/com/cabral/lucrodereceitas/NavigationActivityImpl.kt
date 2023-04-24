package com.cabral.lucrodereceitas

import android.app.Activity
import android.content.Intent
import com.cabral.core.NavigationActivity
import com.cabral.lucrodereceitas.presentation.LoggedActivity
import com.cabral.lucrodereceitas.presentation.NotLoggedActivity

internal class NavigationActivityImpl : NavigationActivity {

    override fun openActivityNotLogged(activity: Activity) {
        activity.finish()
        val intent = Intent(activity, NotLoggedActivity::class.java)
        activity.startActivity(intent)
    }

    override fun openActivityLogged(activity: Activity) {
        activity.finish()
        val intent = Intent(activity, LoggedActivity::class.java)
        activity.startActivity(intent)
    }
}