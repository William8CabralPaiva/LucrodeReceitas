package com.cabral.registeruser.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.cabral.arch.extensions.collectIn
import com.cabral.arch.widget.BorderInputView
import com.cabral.arch.widget.CustomToast
import com.cabral.registeruser.R
import com.cabral.registeruser.databinding.RegisterUserFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterUserFragment : Fragment() {

    private var _binding: RegisterUserFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterUserViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterUserFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initObservers()
    }

    private fun initListeners() {
        binding.run {
            title.setOnClickListener {
                abRegister.finishLoading(false, hideIcon = true)
            }

            abRegister.abSetOnClickListener {
                viewModel.registerUser(
                    biName.getText(),
                    biEmail.getText(),
                    biPassword.getText(),
                    biConfirmPassword.getText()
                )
            }
        }
    }

    private fun initObservers() {


        viewModel.uiEvent.collectIn(this) {
            when (it) {
                is UiEvent.StartLoading -> {
                    binding.abRegister.startLoading()
                }

                is UiEvent.Error -> {
                    binding.abRegister.finishLoading(false, hideIcon = true)
                    showToast(R.string.register_user_already_exists)
                }

                is UiEvent.Success -> {
                    binding.abRegister.finishLoading(true)
                    showToast(R.string.register_user_check_email_conclude_register)
                    requireActivity().onBackPressed()
                }

                is UiEvent.ErrorEmail -> {
                    binding.biEmail.setErrorFocus(it.message)
                }

                is UiEvent.ErrorUsername -> {
                    binding.biName.setErrorFocus(it.message)
                }

                is UiEvent.ErrorPassword -> {
                    binding.biPassword.setErrorFocus(it.message)
                }

                is UiEvent.ErrorConfirmPassword -> {
                    binding.biConfirmPassword.setErrorFocus(it.message)
                }
            }
        }
    }

    private fun BorderInputView.setErrorFocus(text: String) {
        setError(text)
        setFocus()
    }

    private fun showToast(@StringRes string: Int) {
        CustomToast.Builder(requireContext())
            .message(getString(string))
            .build().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}