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
        setDefaultFieldsState()
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
        observeUiEvents()
        observeUiStates()
    }

    private fun observeUiEvents() {
        viewModel.uiEvent.collectIn(this) { event ->
            when (event) {
                is UiEvent.StartLoading -> handleStartLoading()
                is UiEvent.Error -> handleError()
                is UiEvent.Success -> handleSuccess()
            }
        }
    }

    private fun observeUiStates() {
        viewModel.uiState.collectIn(this) { state ->
            when (state) {
                is UiState.ErrorEmail -> binding.biEmail.handleErrorState(state.message)
                is UiState.ErrorUsername -> binding.biName.handleErrorState(state.message)
                is UiState.ErrorPassword -> binding.biPassword.handleErrorState(state.message)
                is UiState.ErrorConfirmPassword -> binding.biConfirmPassword.handleErrorState(state.message)
                UiState.DefaultFieldsState -> Unit
            }
        }
    }

    private fun handleStartLoading() {
        binding.abRegister.startLoading()
    }

    private fun handleError() {
        binding.abRegister.finishLoading(false, hideIcon = true)
        showToast(R.string.register_user_already_exists)
    }

    private fun handleSuccess() {
        binding.abRegister.finishLoading(true)
        showToast(R.string.register_user_check_email_conclude_register)
        requireActivity().onBackPressed()
    }

    private fun BorderInputView.handleErrorState(message: String) {
        setErrorFocus(message)
    }

    private fun BorderInputView.errorInputHasChanged() {
        if (this.isError()) {
            viewModel.setDefaultFieldsState()
        }
    }

    private fun setDefaultFieldsState() {
        allInputs().forEach {
            it.errorInputHasChanged()
        }
    }

    private fun allInputs(): List<BorderInputView> {
        binding.run {
            return listOf(biName, biEmail, biPassword, biConfirmPassword)
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