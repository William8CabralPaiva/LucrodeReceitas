package com.cabral.registeruser.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.cabral.arch.extensions.UserThrowable
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
        viewModel.run {
            notifyStartLoading.observe(viewLifecycleOwner) {
                binding.abRegister.startLoading()
            }

            notifyError.observe(viewLifecycleOwner) {
                binding.abRegister.finishLoading(false, hideIcon = true)
                showToast(R.string.register_user_already_exists)
            }

            notifySuccess.observe(viewLifecycleOwner) {
                binding.abRegister.finishLoading(true)
                showToast(R.string.register_user_check_email_conclude_register)
                requireActivity().onBackPressed()
            }

            notifyErrorUsername.observe(viewLifecycleOwner) {
                binding.biName.setErrorFocus(it)
            }

            notifyErrorEmail.observe(viewLifecycleOwner) {
                binding.biEmail.setErrorFocus(it)
            }

            notifyErrorPassword.observe(viewLifecycleOwner) {
                binding.biPassword.setErrorFocus(it)
            }

            notifyErrorConfirmPassword.observe(viewLifecycleOwner) {
                binding.biConfirmPassword.setErrorFocus(it)
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