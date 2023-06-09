package com.cabral.host.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cabral.features.loggedout.host.databinding.ActivityNotLoggedBinding

class NotLoggedActivity : AppCompatActivity() {

    private var _binding: ActivityNotLoggedBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityNotLoggedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}