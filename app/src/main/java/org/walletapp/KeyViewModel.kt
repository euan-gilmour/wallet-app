package org.walletapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.walletapp.crypto.KeyManager

class KeyViewModel : ViewModel() {

    private var _keyStatus = androidx.compose.runtime.mutableStateOf("Unchecked")
    val keyStatus = _keyStatus

    private var _keySecurityLevel = androidx.compose.runtime.mutableStateOf("Unchecked")
    val keySecurityLevel = _keySecurityLevel

    private var _publicKey = androidx.compose.runtime.mutableStateOf("No Key")
    val publicKey = _publicKey

    fun checkKey() {
        _keyStatus.value = if (KeyManager.keyExists()) "Key Exists" else "No Key"
        _publicKey.value = KeyManager.getPublicKey().toString()
    }

    fun generateKey() {
        viewModelScope.launch(Dispatchers.IO) {
            KeyManager.generateKeys()
            _keyStatus.value = "Key Generated"
        }
    }

    fun deleteKey() {
        viewModelScope.launch(Dispatchers.IO) {
            KeyManager.deleteKey()
            _keyStatus.value = "Key Deleted"
            _publicKey.value = "No Key"
        }
    }

    fun getSecurityLevel() {
        _keySecurityLevel.value = KeyManager.getSecurityLevel()
    }

}