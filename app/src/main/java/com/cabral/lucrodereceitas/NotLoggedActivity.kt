package com.cabral.lucrodereceitas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.cabral.lucrodereceitas.databinding.ActivityNotLoggedBinding

class NotLoggedActivity : AppCompatActivity() {

    private var _binding: ActivityNotLoggedBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNotLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_not_logged) as NavHostFragment

        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(setOf())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}