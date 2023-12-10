package com.cabral.core

import android.app.Activity
import androidx.fragment.app.Fragment

interface NotLoggedNavigation {
    fun openNotLogged(activity: Activity)

    fun openUserRegister(fragment: Fragment)

}