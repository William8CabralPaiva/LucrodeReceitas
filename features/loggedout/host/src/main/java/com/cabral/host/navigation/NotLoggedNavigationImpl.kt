package com.cabral.host.navigation

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cabral.core.NotLoggedNavigation
import com.cabral.features.loggedout.host.R
import com.cabral.host.presentation.NotLoggedActivity

internal class NotLoggedNavigationImpl : NotLoggedNavigation {
    override fun openNotLogged(activity: Activity) {
        activity.finish()
        val intent = Intent(activity, NotLoggedActivity::class.java)
        activity.startActivity(intent)
    }

    override fun openUserRegister(fragment: Fragment) {
        fragment.findNavController().navigate(R.id.loggedout_hostRegisterUserFragment)
    }
}