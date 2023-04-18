package com.cabral.features.splash.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.cabral.core.NavigationUtils
import com.cabral.features.splash.databinding.ActivitySplashScreenBinding

class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            NavigationUtils.splashToLogin(this)
        }, 300)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}