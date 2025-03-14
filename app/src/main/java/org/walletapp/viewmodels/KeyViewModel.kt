package org.walletapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.walletapp.managers.KeyManager

class KeyViewModel : ViewModel() {

    private var _keyStatus = mutableStateOf(if (KeyManager.keyExists()) "Key Exists" else "No Key")
    val keyStatus = _keyStatus

    private var _keySecurityLevel = mutableStateOf(if (KeyManager.keyExists()) KeyManager.getSecurityLevel() else "No Key")
    val keySecurityLevel = _keySecurityLevel

    private var _publicKey = mutableStateOf(if (KeyManager.keyExists()) KeyManager.getPublicKey().toString() else "No Key")
    val publicKey = _publicKey

    fun generateKey() {
        KeyManager.generateKeys()
        _keyStatus.value = "Key Exists"
        _keySecurityLevel.value = KeyManager.getSecurityLevel()
        _publicKey.value = KeyManager.getPublicKey().toString()
    }

    fun deleteKey() {
        KeyManager.deleteKey()
        _keyStatus.value = "No Key"
        _keySecurityLevel.value = "No Key"
        _publicKey.value = "No Key"
    }

}