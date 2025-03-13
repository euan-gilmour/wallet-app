package org.walletapp.auth

import android.hardware.biometrics.BiometricPrompt
import org.walletapp.data.VerifiablePresentationRequest
import org.walletapp.viewmodels.PresentationViewModel

class PresentationBiometricCallback(val onSuccess: () -> Unit) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        onSuccess()
    }

}