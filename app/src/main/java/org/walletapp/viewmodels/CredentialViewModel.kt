package org.walletapp.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.walletapp.connection.ConnectionManager
import org.walletapp.credential.CredentialManager
import org.walletapp.preferences.PreferencesManager

class CredentialViewModel : ViewModel() {

    private val _vc = mutableStateOf(PreferencesManager.getValue("vc") ?: "No VC")
    val vc = _vc

    fun createVc() {
        viewModelScope.launch(Dispatchers.IO) {
            val userDid = PreferencesManager.getValue("did") ?: "No DID"

            _vc.value = CredentialManager.createVerifiableCredentialJwt(userDid)
            PreferencesManager.setValue("vc", _vc.value)
            println(_vc.value)
        }
    }

    fun deleteVc() {
        viewModelScope.launch(Dispatchers.IO) {
            _vc.value = "No VC"
            PreferencesManager.setValue("vc", _vc.value)
        }
    }

    fun initiateCredentialTransfer(scannedValue: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val invitation = JSONObject(scannedValue)

            val webSocketsUrl = invitation.getString("webSocketsUrl")

            ConnectionManager.initiateCredentialTransfer(webSocketsUrl, this@CredentialViewModel)
        }
    }

    fun credentialReceived(vc: String) {
        viewModelScope.launch(Dispatchers.IO){
            _vc.value = vc
            PreferencesManager.setValue("vc", _vc.value)
        }
    }

}