package com.cabral.features.splash.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.cabral.core.LoggedNavigation
import com.cabral.core.NotLoggedNavigation
import com.cabral.features.splash.databinding.ActivitySplashScreenBinding
import org.koin.android.ext.android.inject

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: ActivitySplashScreenBinding? = null
    private val binding get() = _binding!!

    private val navigationLogged: LoggedNavigation by inject()
    private val navigationNotLogged: NotLoggedNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            //navigationLogged.openActivityLogged(this)
            navigationNotLogged.openNotLogged(this)
        }, 300)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}