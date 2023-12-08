package com.cabral.features.presentation

import android.app.Application
import android.os.Bundle
import android.preference.Preference
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.cabral.arch.EmailUtils
import com.cabral.arch.PasswordUtils
import com.cabral.arch.extensions.RecipeThrowable
import com.cabral.arch.getUserKey
import com.cabral.arch.saveUserKey
import com.cabral.core.LoggedNavigation
import com.cabral.features.R
import com.cabral.features.databinding.LoginFragmentBinding
import com.cabral.features.extensions.singInLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.prefs.Preferences

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModel()

    private val navigation: LoggedNavigation by inject()

    private val gso: GoogleSignInOptions by lazy {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
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
            result?.let {
                viewModel.googleEmail(it.email, it.displayName)
            }

        })

        binding.googleLogin.setOnClickListener {
            val signInIntent = gsc.signInIntent
            launcher.launch(signInIntent)
        }

        binding.acEnter.abSetOnClickListener {
            login()
        }

        viewModel.notifySuccess.observe(viewLifecycleOwner) {
            it.key?.let { it1 -> context?.saveUserKey(it1) }
            navigation.openActivityLogged(requireActivity())
        }

        viewModel.notifyError.observe(viewLifecycleOwner) {
            Toast.makeText(
                context,
                getString(R.string.login_check_internet),
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun login() {
        try {
            if (EmailUtils.validateEmail(binding.biEmail.getText()) &&
                PasswordUtils.validatePassword(binding.biPassword.getText())
            ) {
                viewModel.login(binding.biEmail.getText(), binding.biPassword.getText())
            }
        } catch (e: RecipeThrowable) {
            if (e is RecipeThrowable.AuthenticateEmail) {
                binding.biEmail.setError(e.message)
            } else {
                binding.biPassword.setError(e.message)
            }
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