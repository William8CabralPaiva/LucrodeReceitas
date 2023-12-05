package com.cabral.features.extensions

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task

fun Fragment.singInLauncher(
    successBlock: Task<GoogleSignInAccount>.() -> Unit,
    failureBlock: Throwable.() -> Unit = {}
): ActivityResultLauncher<Intent> {
    return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            runCatching { GoogleSignIn.getSignedInAccountFromIntent(it.data) }
                .onFailure(failureBlock::invoke)
                .onSuccess(successBlock::invoke)
        } else {
            failureBlock(Throwable("Result not found"))
        }
    }
}