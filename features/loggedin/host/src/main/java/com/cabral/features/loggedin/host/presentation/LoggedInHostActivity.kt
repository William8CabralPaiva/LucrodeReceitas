package com.cabral.features.loggedin.host.presentation

import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cabral.features.loggedin.host.R
import com.cabral.features.loggedin.host.databinding.LoggedinHostActivityBinding

class LoggedInHostActivity : AppCompatActivity() {

    private var _binding: LoggedinHostActivityBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LoggedinHostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_logged) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavMain.setupWithNavController(navController)

        appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    com.cabral.listrecipe.R.id.list_recipe,
                    R.id.list_ingredients,
                    com.cabral.profile.R.id.profileFragment
                )
            )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevelDestination =
                appBarConfiguration.topLevelDestinations.contains(destination.id)

            binding.bottomNavMain.isVisible = isTopLevelDestination
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun setBadgeInIcon(@IntegerRes id: Int, amount: Int) {
//        binding.bottomNavMain.run {
//            val badge = getOrCreateBadge(id)
//            badge.backgroundColor = getColor(R.color.black)
//            badge.badgeGravity = BadgeDrawable.TOP_END
//            badge.number = amount
//        }
    }
}