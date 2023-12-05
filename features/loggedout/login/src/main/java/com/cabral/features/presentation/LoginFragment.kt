package com.cabral.features.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cabral.core.LoggedNavigation
import com.cabral.features.databinding.LoginFragmentBinding
import com.cabral.features.extensions.singInLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()

    private val navigation: LoggedNavigation by inject()

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    }

    private val gsc: GoogleSignInClient by lazy { GoogleSignIn.getClient(requireActivity(), gso) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val launcher = singInLauncher(successBlock = {
            this.result.email
        })

        binding.googleLogin.setOnClickListener {
            val signInIntent = gsc.signInIntent
            launcher.launch(signInIntent)
        }

        viewModel.notifySuccess.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Adicionou", Toast.LENGTH_LONG).show()
        }

        viewModel.notifyError.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Falha ao add", Toast.LENGTH_LONG).show()
        }

        binding.imageView.setOnClickListener {
            // viewModel.addUser()
            viewModel.login()
            // navigation.openActivityLogged(requireActivity())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = LoginFragment()
    }

}