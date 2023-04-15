package com.cabral.lucrodereceitas

import android.os.Bundle
import androidx.annotation.IntegerRes
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cabral.lucrodereceitas.databinding.ActivityLoggedBinding
import com.google.android.material.badge.BadgeDrawable

class LoggedActivity : AppCompatActivity() {

    private var _binding: ActivityLoggedBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_logged) as NavHostFragment

        navController = navHostFragment.navController

        binding.bottomNavMain.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(setOf())

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


    fun setBadgeInIcon(@IntegerRes id: Int, amount: Int) {
        binding.bottomNavMain.run {
            val badge = getOrCreateBadge(id)
            badge.backgroundColor = getColor(R.color.black)
            badge.badgeGravity = BadgeDrawable.TOP_END
            badge.number = amount
        }
    }
}