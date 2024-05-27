package com.cabral.features.splash.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cabral.arch.getUserKey
import com.cabral.core.LoggedNavigation
import com.cabral.core.NotLoggedNavigation
import com.cabral.features.splash.databinding.SplashScreenActivityBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private var _binding: SplashScreenActivityBinding? = null

    private val viewModel: SplashScreenViewModel by viewModel()
    private val binding get() = _binding!!

    private val navigationLogged: LoggedNavigation by inject()
    private val navigationNotLogged: NotLoggedNavigation by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = SplashScreenActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initObservers()
        viewModel.getUserLogged(this.getUserKey())
    }

    private fun initObservers() {
        viewModel.notifyLogged.observe(this) {
            if (it) {
                navigationLogged.openActivityLogged(this)
            } else {
                navigationNotLogged.openNotLogged(this)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}