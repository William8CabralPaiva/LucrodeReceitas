package com.cabral.config.presentation

import android.os.Bundle
import android.view.View
import com.cabral.arch.BaseFragment
import com.cabral.arch.restartApp
import com.cabral.config.databinding.ConfigFragmentBinding
import com.cabral.core.common.SingletonUser

class ConfigFragment : BaseFragment<ConfigFragmentBinding>(ConfigFragmentBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardView.setOnClickListener {
            SingletonUser.getInstance().reset()
            activity?.restartApp()
        }
    }

}