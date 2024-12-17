package com.cabral.arch

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CredentialOption
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import com.cabral.arch.extensions.GenericThrowable
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object GoogleSignInUtils {

    fun doGoogleSignIn(
        context: Context,
        scope: CoroutineScope,
        login: (email: String?, displayName: String?) -> Unit,
        error: (throwable: GenericThrowable.FailThrowable) -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(getCredentialOptions(context))
            .build()
        scope.launch {
            try {
                val result = credentialManager.getCredential(context, request)
                when (result.credential) {
                    is CustomCredential -> {
                        if (result.credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                            val googleIdTokenCredential =
                                GoogleIdTokenCredential.createFrom(result.credential.data)
                            val googleTokenId = googleIdTokenCredential.idToken
                            val authCredential =
                                GoogleAuthProvider.getCredential(googleTokenId, null)
                            val user =
                                Firebase.auth.signInWithCredential(authCredential).await().user
                            user?.run {
                                if (isAnonymous.not()) {
                                    login.invoke(email, displayName)
                                }
                            }
                        }
                    }

                    else -> {
                        error.invoke(GenericThrowable.FailThrowable())
                    }
                }
            } catch (e: NoCredentialException) {
                error.invoke(GenericThrowable.FailThrowable())
            } catch (e: GetCredentialException) {
                error.invoke(GenericThrowable.FailThrowable())
            }
        }
    }


    private fun getCredentialOptions(context: Context): CredentialOption {
        return GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .setServerClientId(context.getString(R.string.arch_web_client_id))
            .build()
    }
}
