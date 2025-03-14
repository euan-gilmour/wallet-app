package org.walletapp.auth

import android.hardware.biometrics.BiometricPrompt

class PresentationBiometricCallback(val onSuccess: () -> Unit) : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        onSuccess()
    }

}