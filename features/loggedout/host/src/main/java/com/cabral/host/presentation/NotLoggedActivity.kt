package com.cabral.host.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cabral.features.loggedout.host.databinding.LoggedoutHostActivityBinding

class NotLoggedActivity : AppCompatActivity() {

    private var _binding: LoggedoutHostActivityBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = LoggedoutHostActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}