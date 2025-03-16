package org.walletapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.walletapp.managers.KeyManager

/**
 * A ViewModel for the Key Management tab..
 *
 * This ViewModel manages the key status, key security level, and public key, as well as
 * functionality for generating and deleting keypairs.
 */
class KeyViewModel : ViewModel() {

    private var _keyStatus = mutableStateOf(if (KeyManager.keyExists()) "Key Exists" else "No Key")
    val keyStatus = _keyStatus

    private var _keySecurityLevel =
        mutableStateOf(if (KeyManager.keyExists()) KeyManager.getSecurityLevel() else "No Key")
    val keySecurityLevel = _keySecurityLevel

    private var _publicKey = mutableStateOf(
        if (KeyManager.keyExists()) KeyManager.getPublicKey().toString() else "No Key"
    )
    val publicKey = _publicKey

    /**
     * Generate a new key and update the key status, security level, and public key.
     */
    fun generateKey() {
        KeyManager.generateKeys()
        _keyStatus.value = "Key Exists"
        _keySecurityLevel.value = KeyManager.getSecurityLevel()
        _publicKey.value = KeyManager.getPublicKey().toString()
    }

    /**
     * Delete the current keypair and update the key status, security level, and public key.
     */
    fun deleteKey() {
        KeyManager.deleteKey()
        _keyStatus.value = "No Key"
        _keySecurityLevel.value = "No Key"
        _publicKey.value = "No Key"
    }

}