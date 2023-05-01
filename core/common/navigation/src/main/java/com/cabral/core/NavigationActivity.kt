package com.cabral.core

import android.app.Activity

interface NavigationActivity {

    fun openActivityNotLogged(activity: Activity)

    fun openActivityLogged(activity: Activity)

}