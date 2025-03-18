package org.walletapp.auth

import android.hardware.biometrics.BiometricPrompt

/**
 * A simple authentication callback for use in creating Verifiable Presentations
 *
 * @property onSuccess the callback to execute when authentication is successful
 */
class PresentationBiometricCallback(val onSuccess: () -> Unit) : BiometricPrompt.AuthenticationCallback() {

    /**
     * On successful authentication, call the onSuccess callback
     *
     * @param result the result of the authentication
     */
    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        onSuccess()
    }

}